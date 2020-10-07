package pl.homeweather.weatherstation.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class DoubleToolsTest {

    @ParameterizedTest(name = "For a given double value, method should return {1}")
    @CsvSource({
            "0.0, 0.0",
            "0.199, 0.20",
            "0.001, 0.0",
            "0.005, 0.01",
            "-1, -1.0",
            "234.23456, 234.23"
    })
    void roundDoubleTest(double given, double expected) {
        //when
        Double rounded = DoubleTools.roundDouble(given, 2);

        //then
        assertEquals(rounded, expected);
    }

    @Test
    void roundDoubleNullTest() {
        //when
        Double rounded = DoubleTools.roundDouble(null, 2);

        //then
        assertEquals(null, rounded);
    }
}