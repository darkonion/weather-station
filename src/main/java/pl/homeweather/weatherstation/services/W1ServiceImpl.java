package pl.homeweather.weatherstation.services;

import com.pi4j.component.temperature.TemperatureSensor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class W1ServiceImpl implements W1Service {

    private final W1SensorDiscoveryService w1Discovery;

    public W1ServiceImpl(W1SensorDiscoveryService w1Discovery) {
        this.w1Discovery = w1Discovery;
    }

    @Override
    public Double getTemperatureMeasurement() {
       Optional<TemperatureSensor> tempSensor = w1Discovery.getTemperatureSensor();

       if (!tempSensor.isPresent()) {
           return null;
       }
       return tempSensor.get().getTemperature();
    }
}
