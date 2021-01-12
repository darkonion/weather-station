package pl.homeweather.weatherstation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.homeweather.weatherstation.dtos.SensorSettings;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
public class SettingsService {

    private final WebClient webClient;
    private final String SETTINGS_POLLING_URI = "/settings/sensors";

    public SettingsService(WebClient webClient) {
        this.webClient = webClient;
    }

    public SensorSettings getCurrentSettings() {
        return requireNonNull(webClient.get().uri(SETTINGS_POLLING_URI)
                .exchange()
                .doOnError(e -> log.error("Failed to get current sensors settings"))
                .block()).bodyToMono(SensorSettings.class)
                .block();
    }
}
