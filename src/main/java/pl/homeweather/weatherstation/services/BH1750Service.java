package pl.homeweather.weatherstation.services;

import com.pi4j.io.i2c.I2CBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.homeweather.weatherstation.drivers.BH1750FVIDriver;

import java.io.IOException;

@Slf4j
@Service
public class BH1750Service {

    public Float measureLux() {
        try {
            BH1750FVIDriver bh1750fvi = BH1750FVIDriver
                    .getInstance(I2CBus.BUS_1, BH1750FVIDriver.I2C_ADDRESS_23);
            bh1750fvi.open();
            return bh1750fvi.getOptical();
        } catch (IOException e) {
            log.info("Troubles with connection to BME280 sensor, error: " + e.getMessage());
            return null;
        }
    }
}
