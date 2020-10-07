package pl.homeweather.weatherstation.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AirPurityMeasurement {

    private final Integer pm1;
    private final Integer pm25;
    private final Integer pm10;

    private final LocalDateTime date = LocalDateTime.now();
}