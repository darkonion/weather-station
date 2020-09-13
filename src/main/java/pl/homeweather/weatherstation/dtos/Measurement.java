package pl.homeweather.weatherstation.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Measurement {

    @Builder.Default
    private final Double temperature = null;
    private final LocalDateTime date = LocalDateTime.now();
}
