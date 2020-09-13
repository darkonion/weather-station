package pl.homeweather.weatherstation.services;

import com.pi4j.component.temperature.TemperatureSensor;

import java.util.Optional;

public interface W1SensorDiscoveryService {

    Optional<TemperatureSensor> getTemperatureSensor(int maxRetryAmount);
}
