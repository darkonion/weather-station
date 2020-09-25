package pl.homeweather.weatherstation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.homeweather.weatherstation.dtos.BasicMeasurement;
import pl.homeweather.weatherstation.dtos.PMS7003Measurement;
import pl.homeweather.weatherstation.services.BasicMeasurementAssemblyService;
import pl.homeweather.weatherstation.services.PMS7003Service;

import java.util.concurrent.ExecutionException;


@RestController
public class MainController {

    private final BasicMeasurementAssemblyService assemblyService;
    private final PMS7003Service pms7003Service;


    public MainController(BasicMeasurementAssemblyService assemblyService, PMS7003Service pms7003Service) {
        this.assemblyService = assemblyService;
        this.pms7003Service = pms7003Service;
    }

    @GetMapping("/basic")
    public BasicMeasurement getTemperature() {
        return assemblyService.assembleBasicMeasurement();
    }

    @GetMapping("/air")
    public PMS7003Measurement getAirQuality() {
        return pms7003Service.getMeasurement();
    }
}
