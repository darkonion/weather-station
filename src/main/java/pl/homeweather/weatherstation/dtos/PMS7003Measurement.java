package pl.homeweather.weatherstation.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PMS7003Measurement {

    private LocalDateTime time;

    private int pm1_0_cf1;
    private int pm2_5_cf1;
    private int pm10_0_cf1;
    private int pm1_0_atmo;
    private int pm2_5_atmo;
    private int pm10_0_atmo;
    private int pm0_3_count;
    private int pm0_5_count;
    private int pm1_0_count;
    private int pm2_5_count;
    private int pm5_0_count;
    private int pm10_0_count;

}