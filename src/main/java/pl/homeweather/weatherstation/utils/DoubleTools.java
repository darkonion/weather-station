package pl.homeweather.weatherstation.utils;

public interface DoubleTools {

    static Double roundDouble(Double value, int places) {
        if (value == null) return null;
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
