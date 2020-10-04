package pl.homeweather.weatherstation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.homeweather.weatherstation.dtos.BasicMeasurement;

import static java.util.Optional.ofNullable;
import static pl.homeweather.weatherstation.utils.DoubleTools.roundDouble;

@Slf4j
@Service
public class BasicMeasurementAssemblyService {

    private final W1TemperatureService temperatureServiceService;
    private final BME280Service bme280Service;
    private final BH1750Service bh1750Service;

    public BasicMeasurementAssemblyService(W1TemperatureService temperatureServiceService, BME280Service bme280Service, BH1750Service bh1750Service) {
        this.temperatureServiceService = temperatureServiceService;
        this.bme280Service = bme280Service;
        this.bh1750Service = bh1750Service;
    }

    public BasicMeasurement getMeasurement() {
        Double[] bme280Measurements = bme280Service.getMeasurement();

        BasicMeasurement basicMeasurement = BasicMeasurement
                .builder()
                .temperature(temperatureServiceService.getMeasurement())
                .pressure(roundDouble(bme280Measurements[0], 2))
                .humidity(roundDouble(bme280Measurements[1], 2))
                .lux(roundDouble(
                        ofNullable(bh1750Service.getMeasurement())
                                .map(Float::doubleValue)
                                .orElse(null), 2))
                .build();

        log.info("Basic measurement: " + basicMeasurement.toString());
        return basicMeasurement;
    }
}
