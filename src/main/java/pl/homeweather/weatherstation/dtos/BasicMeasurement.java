package pl.homeweather.weatherstation.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BasicMeasurement {

    private final Double temperature;
    private final Double pressure;
    private final Double humidity;
    private final Double lux;

    private final LocalDateTime date = LocalDateTime.now();
}
