package pl.homeweather.weatherstation.utils;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.util.Optional.ofNullable;

public interface DoubleTools {

    static Double roundDouble(Double value, int places) {
        double scale = pow(10, places);
        return ofNullable(value)
                .map(v -> round(v * scale) / scale)
                .orElse(null);
    }
}
