package pl.homeweather.weatherstation.services;

import pl.homeweather.weatherstation.dtos.Measurement;

public interface W1Service {

    Measurement getTemperatureMeasurement();
}
