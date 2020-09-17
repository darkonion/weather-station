package pl.homeweather.weatherstation.services;

import com.pi4j.io.i2c.I2CFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.homeweather.weatherstation.drivers.BME280Driver;

import java.io.IOException;

@Slf4j
@Getter
@Service
public class BME280Service {

    private final BME280Driver driver = new BME280Driver();

    public Double[] performMeasurement() {
        try {
            return driver.readMeasurements();
        } catch (I2CFactory.UnsupportedBusNumberException | IOException e) {
            log.info("Troubles with connection to BME280 sensor, error: " + e.getMessage());
            return new Double[3];
        }
    }
}
