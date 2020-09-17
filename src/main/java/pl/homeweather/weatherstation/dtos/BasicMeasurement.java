package pl.homeweather.weatherstation.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BasicMeasurement {

    @Builder.Default
    private final Double temperature = null;

    @Builder.Default
    private final Double pressure = null;

    @Builder.Default
    private final Double humidity = null;

    @Builder.Default
    private final Double lux = null;

    private final LocalDateTime date = LocalDateTime.now();
}
