package pl.homeweather.weatherstation.runnables;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pl.homeweather.weatherstation.dtos.AirPurityMeasurement;
import pl.homeweather.weatherstation.services.PMS7003Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
public class AirMeasurementCall implements Runnable {

    private final WebClient webClient;
    private final PMS7003Service airPurityService;

    private static final String AIR_MEASUREMENT_URI = "/api/air";

    public AirMeasurementCall(WebClient webClient, PMS7003Service airPurityService) {
        this.webClient = webClient;
        this.airPurityService = airPurityService;
    }


    @Override
    public void run() {
        try {
            airMeasurementPostCall(airPurityService.getMeasurement());
        } catch (Exception e) {
            errorHandling(e);
        }
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
