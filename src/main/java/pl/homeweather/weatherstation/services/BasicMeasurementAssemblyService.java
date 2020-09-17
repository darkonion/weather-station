package pl.homeweather.weatherstation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.homeweather.weatherstation.dtos.BasicMeasurement;

import static java.util.Optional.ofNullable;
import static pl.homeweather.weatherstation.utils.DoubleTools.roundDouble;

@Slf4j
@Service
public class BasicMeasurementAssemblyService {

    private final W1Service w1Service;
    private final BME280Service bme280Service;
    private final BH1750Service bh1750Service;

    public BasicMeasurementAssemblyService(W1Service w1Service, BME280Service bme280Service, BH1750Service bh1750Service) {
        this.w1Service = w1Service;
        this.bme280Service = bme280Service;
        this.bh1750Service = bh1750Service;
    }

    public BasicMeasurement assembleBasicMeasurement() {
        Double[] bme280Measurements = bme280Service.performMeasurement();

        BasicMeasurement basicMeasurement = BasicMeasurement
                .builder()
                .temperature(w1Service.getMeasurement())
                .pressure(roundDouble(bme280Measurements[0], 2))
                .humidity(roundDouble(bme280Measurements[1], 2))
                .lux(roundDouble(
                        ofNullable(bh1750Service.measureLux())
                        .map(Float::doubleValue)
                        .orElse(null), 2))
                .build();

        log.info("Basic measurement: " + basicMeasurement.toString());
        return basicMeasurement;
    }
}
