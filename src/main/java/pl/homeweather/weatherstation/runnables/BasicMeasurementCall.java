package pl.homeweather.weatherstation.runnables;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pl.homeweather.weatherstation.dtos.BasicMeasurement;
import pl.homeweather.weatherstation.services.BasicMeasurementAssemblyService;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
public class BasicMeasurementCall implements Runnable {

    private final WebClient webClient;
    private final BasicMeasurementAssemblyService basicMeasurementService;

    private static final String BASIC_MEASUREMENT_URI = "/api/basic";

    public BasicMeasurementCall(WebClient webClient, BasicMeasurementAssemblyService basicMeasurementService) {
        this.webClient = webClient;
        this.basicMeasurementService = basicMeasurementService;
    }


    @Override
    public void run() {
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

    private void errorHandling(Exception e) {
        log.info("Cannot connect to Weather Harvester, error: {}", e.getMessage());
        log.debug(Arrays.toString(e.getStackTrace()));
    }
}
