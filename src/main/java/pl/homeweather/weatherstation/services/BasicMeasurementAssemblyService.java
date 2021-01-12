package pl.homeweather.weatherstation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.homeweather.weatherstation.dtos.BasicMeasurement;
import pl.homeweather.weatherstation.dtos.SensorSettings;

import static java.util.Optional.ofNullable;
import static pl.homeweather.weatherstation.utils.DoubleTools.roundDouble;

@Slf4j
@Service
public class BasicMeasurementAssemblyService {

    private final W1TemperatureService temperatureServiceService;
    private final BME280Service bme280Service;
    private final BH1750Service bh1750Service;
    private final SettingsService settingsService;

    private SensorSettings settings;

    public BasicMeasurementAssemblyService(W1TemperatureService temperatureServiceService, BME280Service bme280Service, BH1750Service bh1750Service, SettingsService settingsService) {
        this.temperatureServiceService = temperatureServiceService;
        this.bme280Service = bme280Service;
        this.bh1750Service = bh1750Service;
        this.settingsService = settingsService;
    }

    public BasicMeasurement getMeasurement() {
        getCurrentSensorSettings();

        Double[] bme280Measurements = bme280Service.getMeasurement();

        BasicMeasurement basicMeasurement = BasicMeasurement
                .builder()
                .temperature(resolveTemperature(bme280Measurements[2]))
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

    private void getCurrentSensorSettings() {
        try {
            settings = settingsService.getCurrentSettings();
        } catch (Exception e) {
            log.error("Failed during call for sensor settings, keeping default values. Error: " + e.getMessage());
            settings =  new SensorSettings();
        }
        log.info("Sensor settings: " + settings.toString());
    }

    private Double resolveTemperature(Double temperature) {
        switch (settings.getTemperature()) {
            case BME280:
                return roundDouble(temperature, 2);
            case W1:
            default:
                return temperatureServiceService.getMeasurement();
        }
    }
}
