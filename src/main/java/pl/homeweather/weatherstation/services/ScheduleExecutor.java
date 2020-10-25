package pl.homeweather.weatherstation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.homeweather.weatherstation.dtos.Cron;

import javax.annotation.PostConstruct;
import java.util.Objects;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
public class ScheduleExecutor {

    @Value("${station.air-cron}")
    private String defaultAirCron;

    @Value("${station.basic-cron}")
    private String defaultBasicCron;

    private String airCron = null;
    private String basicCron = null;

    private final WebClient webClient;
    private final SchedulingService schedulingService;

    private static final String CRON_POLLING_URI = "/settings/cron";


    public ScheduleExecutor(WebClient webClient, SchedulingService schedulingService) {
        this.webClient = webClient;
        this.schedulingService = schedulingService;
    }

    @PostConstruct
    private void initCron() {
        log.info("Initializing cron");
        getCron();
        schedulingService.scheduleAirMeasurement(getAirCron());
        schedulingService.scheduleBasicMeasurement(getBasicCron());
    }

    public String getAirCron() {
        return ofNullable(airCron).orElse(defaultAirCron);
    }

    public String getBasicCron() {
        return ofNullable(basicCron).orElse(defaultBasicCron);
    }

    private void getCron() {
        Cron currentCron = tryGetCurrentCron();
        if (isCroneNotEmpty(currentCron)) {
            airCron = currentCron.getAirCron();
            basicCron = currentCron.getBasicCron();
            log.info("Basic Cron: {}, Air Cron: {}", basicCron, airCron);
        } else {
            log.info("Crone corrupted/empty, going back to default settings!");
        }
    }

    @Scheduled(fixedRate = 1000 * 60)
    private void restartIfCroneChanged() {
        Cron currentCron = tryGetCurrentCron();
        if (isCroneNotEmpty(currentCron) && isCroneChanged(currentCron)) {
            airCron = currentCron.getAirCron();
            basicCron = currentCron.getBasicCron();
            log.info("Cron settings changed! Basic Cron: {}, Air Cron: {}", basicCron, airCron);
            newSchedule();
        }
    }

    private void newSchedule() {
        schedulingService.cancelBasicMeasurementTask();
        schedulingService.cancelAirMeasurementTask();

        schedulingService.scheduleBasicMeasurement(getBasicCron());
        schedulingService.scheduleAirMeasurement(getAirCron());
    }

    private Cron tryGetCurrentCron() {
        Cron currentCron;
        try {
            currentCron = getCurrentCron();
        } catch (Exception e) {
            log.info("Failed during call for cron");
            currentCron = new Cron();
        }
        return currentCron;
    }

    private Cron getCurrentCron() {
        return requireNonNull(webClient.get().uri(CRON_POLLING_URI)
                .exchange()
                .doOnError(e -> log.error("Failed to get current cron settings"))
                .block()).bodyToMono(Cron.class)
                .block();
    }

    private boolean isCroneChanged(Cron cron) {
        return !Objects.equals(cron.getAirCron(), airCron) || !Objects.equals(cron.getBasicCron(), basicCron);
    }

    private boolean isCroneNotEmpty(Cron cron) {
        return nonNull(cron.getAirCron()) && nonNull(cron.getBasicCron());
    }
}
