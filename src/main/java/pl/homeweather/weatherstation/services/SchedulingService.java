package pl.homeweather.weatherstation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.homeweather.weatherstation.runnables.AirMeasurementCall;
import pl.homeweather.weatherstation.runnables.BasicMeasurementCall;

import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class SchedulingService {

    private final TaskScheduler taskScheduler;
    private final WebClient webClient;
    private final BasicMeasurementAssemblyService basicMeasurementAssemblyService;
    private final PMS7003Service pms7003Service;

    private ScheduledFuture<?> airMeasurementTask;
    private ScheduledFuture<?> basicMeasurementTask;

    public SchedulingService(TaskScheduler taskScheduler, WebClient webClient, BasicMeasurementAssemblyService basicMeasurementAssemblyService, PMS7003Service pms7003Service) {
        this.taskScheduler = taskScheduler;
        this.webClient = webClient;
        this.basicMeasurementAssemblyService = basicMeasurementAssemblyService;
        this.pms7003Service = pms7003Service;
    }

    public void scheduleBasicMeasurement(String cron) {
        log.info("Firing new Basic Measurement Task with cron: {}", cron);
        basicMeasurementTask = taskScheduler
                .schedule(
                        new BasicMeasurementCall(webClient, basicMeasurementAssemblyService), new CronTrigger(cron)
                );
    }

    public void scheduleAirMeasurement(String cron) {
        log.info("Firing new Air Measurement Task with cron: {}", cron);
        airMeasurementTask = taskScheduler
                .schedule(
                        new AirMeasurementCall(webClient, pms7003Service), new CronTrigger(cron)
                );
    }

    public void cancelBasicMeasurementTask() {
        log.info("Canceling Basic Measurement Task");
        basicMeasurementTask.cancel(true);
    }

    public void cancelAirMeasurementTask() {
        log.info("Canceling Air Measurement Task");
        airMeasurementTask.cancel(true);
    }
}
