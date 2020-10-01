package pl.homeweather.weatherstation.transmitters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pl.homeweather.weatherstation.dtos.AirPurityMeasurement;
import pl.homeweather.weatherstation.dtos.BasicMeasurement;
import pl.homeweather.weatherstation.services.BasicMeasurementAssemblyService;
import pl.homeweather.weatherstation.services.PMS7003Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@Service
public class MeasurementHttpCaller {

    private final WebClient webClient;
    private final BasicMeasurementAssemblyService basicMeasurementService;
    private final PMS7003Service airPurityService;

    private static final String BASIC_MEASUREMENT_URI = "/basic";
    private static final String AIR_MEASUREMENT_URI = "/air";

    public MeasurementHttpCaller(WebClient webClient, BasicMeasurementAssemblyService basicMeasurementService, PMS7003Service airPurityService) {
        this.webClient = webClient;
        this.basicMeasurementService = basicMeasurementService;
        this.airPurityService = airPurityService;
    }

    @Scheduled(cron = "${station.air-cron}")
    public void sendAirMeasurement() {
        try {
            airMeasurementPostCall(airPurityService.getMeasurement());
        } catch (Exception e) {
            log.info("Cannot connect to Weather Harvester, error: {}", e.getMessage());
            log.debug(Arrays.toString(e.getStackTrace()));
        }
    }

    @Scheduled(cron = "${station.basic-cron}")
    public void sendBasicMeasurement() {
        try {
            basicMeasurementPostCall(basicMeasurementService.assembleBasicMeasurement());
        } catch (Exception e) {
            log.info("Cannot connect to Weather Harvester, error: {}", e.getMessage());
            log.debug(Arrays.toString(e.getStackTrace()));
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
}
