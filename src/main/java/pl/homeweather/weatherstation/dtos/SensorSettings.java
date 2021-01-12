package pl.homeweather.weatherstation.dtos;

import lombok.Data;
import pl.homeweather.weatherstation.utils.Sensor;

@Data
public class SensorSettings {

    private Long id;
    private Sensor temperature = Sensor.W1;
}
