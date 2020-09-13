package pl.homeweather.weatherstation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.homeweather.weatherstation.dtos.Measurement;
import pl.homeweather.weatherstation.services.W1Service;


@RestController
public class MainController {

    private final W1Service w1Service;

    public MainController(W1Service w1Service) {
        this.w1Service = w1Service;
    }

    @GetMapping("temp")
    public Measurement getTemperature() {
        return w1Service.getTemperatureMeasurement();
    }
}
