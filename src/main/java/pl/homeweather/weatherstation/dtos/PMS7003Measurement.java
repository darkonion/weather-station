package pl.homeweather.weatherstation.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PMS7003Measurement {

    private LocalDateTime time;

    private Integer pm1_0_cf1;
    private Integer pm2_5_cf1;
    private Integer pm10_0_cf1;
    private Integer pm1_0_atmo;
    private Integer pm2_5_atmo;
    private Integer pm10_0_atmo;
    private Integer pm0_3_count;
    private Integer pm0_5_count;
    private Integer pm1_0_count;
    private Integer pm2_5_count;
    private Integer pm5_0_count;
    private Integer pm10_0_count;
}