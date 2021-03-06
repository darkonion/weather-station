package pl.homeweather.weatherstation.services;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
@Service
public class W1TemperatureService {

    private final W1Master w1Master;

    public W1TemperatureService(W1Master w1Master) {
        this.w1Master = w1Master;
    }

    public Double getMeasurement() {
       return getTemperatureSensor()
               .map(TemperatureSensor::getTemperature)
               .orElse(null);
    }

    private Optional<TemperatureSensor> getTemperatureSensor() {
        try {
            return of(w1Master.getDevices(TemperatureSensor.class).get(0));
        } catch (IndexOutOfBoundsException e) {
            log.info("Temperature sensor currently unavailable!");
            return empty();
        }
    }
}
