package pl.homeweather.weatherstation.services;

import com.pi4j.component.temperature.TemperatureSensor;
import org.springframework.stereotype.Service;
import pl.homeweather.weatherstation.dtos.Measurement;

import java.util.Optional;


@Service
public class W1ServiceImpl implements W1Service {

    private final W1SensorDiscoveryService w1Discovery;

    public W1ServiceImpl(W1SensorDiscoveryService w1Discovery) {
        this.w1Discovery = w1Discovery;
    }

    @Override
    public Measurement getTemperatureMeasurement() {
       Optional<TemperatureSensor> tempSensor = w1Discovery.getTemperatureSensor(2);

       if (tempSensor.isEmpty()) {
           return Measurement.builder().build();
       }
       return Measurement.builder()
               .temperature(tempSensor.get().getTemperature())
               .build();
    }
}
