package pl.homeweather.weatherstation.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PMS7003Measurement {

    private LocalDateTime date;

    private Integer pm1;
    private Integer pm25;
    private Integer pm10;
}