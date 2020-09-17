package pl.homeweather.weatherstation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.homeweather.weatherstation.dtos.BasicMeasurement;
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
}
