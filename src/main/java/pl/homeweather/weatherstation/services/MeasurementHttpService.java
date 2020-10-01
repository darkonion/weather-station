package pl.homeweather.weatherstation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pl.homeweather.weatherstation.dtos.AirPurityMeasurement;
import pl.homeweather.weatherstation.dtos.BasicMeasurement;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@Service
public class MeasurementHttpService {

    private final WebClient webClient;
    private final BasicMeasurementAssemblyService basicMeasurementService;
    private final PMS7003Service airPurityService;

    private static final String BASIC_MEASUREMENT_URI = "/basic";
    private static final String AIR_MEASUREMENT_URI = "/air";

    public MeasurementHttpService(WebClient webClient,
                                  BasicMeasurementAssemblyService basicMeasurementService,
                                  PMS7003Service airPurityService) {
        this.webClient = webClient;
        this.basicMeasurementService = basicMeasurementService;
        this.airPurityService = airPurityService;
    }

    @Scheduled(cron = "${station.air-cron}")
    public void sendAirMeasurement() {
        try {
            airMeasurementPostCall(airPurityService.getMeasurement());
        } catch (Exception e) {
            errorHandling(e);
        }
    }

    @Scheduled(cron = "${station.basic-cron}")
    public void sendBasicMeasurement() {
        try {
            basicMeasurementPostCall(basicMeasurementService.getMeasurement());
        } catch (Exception e) {
            errorHandling(e);
        }
    }

    private void basicMeasurementPostCall(BasicMeasurement basicMeasurement) {
        webClient.post().uri(BASIC_MEASUREMENT_URI)
                .body(BodyInserters.fromPublisher(Mono.just(basicMeasurement), BasicMeasurement.class))
                .exchange()
                .doOnSuccess(s -> log.info("Basic measurement was successfully send!"))
                .doOnError(e -> log.error("Failed to send current measurement"))
                .subscribe();
    }

    private void airMeasurementPostCall(AirPurityMeasurement airMeasurement) {
        webClient.post().uri(AIR_MEASUREMENT_URI)
                .body(BodyInserters.fromPublisher(Mono.just(airMeasurement), AirPurityMeasurement.class))
                .exchange()
                .doOnSuccess(s -> log.info("Air measurement was successfully send!"))
                .doOnError(e -> log.error("Failed to send current measurement"))
                .subscribe();
    }

    private void errorHandling(Exception e) {
        log.info("Cannot connect to Weather Harvester, error: {}", e.getMessage());
        log.debug(Arrays.toString(e.getStackTrace()));
    }
}
