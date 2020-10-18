package pl.homeweather.weatherstation.dtos;

import lombok.Data;

@Data
public final class Cron {

    private Long id;
    private String airCron;
    private String basicCron;
}
