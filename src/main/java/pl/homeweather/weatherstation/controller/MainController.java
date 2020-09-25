package pl.homeweather.weatherstation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.homeweather.weatherstation.drivers.PMS7003Driver;
import pl.homeweather.weatherstation.dtos.BasicMeasurement;
import pl.homeweather.weatherstation.dtos.PMS7003Measurement;
import pl.homeweather.weatherstation.services.BasicMeasurementAssemblyService;


@RestController
public class MainController {

    private final BasicMeasurementAssemblyService assemblyService;


    public MainController(BasicMeasurementAssemblyService assemblyService) {
        this.assemblyService = assemblyService;
    }

    @GetMapping("/basic")
    public BasicMeasurement getTemperature() {
        return assemblyService.assembleBasicMeasurement();
    }

    @GetMapping("/test")
    public String getAirQuality() {
        PMS7003Driver driver = new PMS7003Driver();
        driver.activate();
        PMS7003Measurement measure = driver.measure();
        return measure.toString();
    }
}
