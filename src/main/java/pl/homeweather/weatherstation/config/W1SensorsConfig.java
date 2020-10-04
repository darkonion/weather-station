package pl.homeweather.weatherstation.config;

import com.pi4j.io.w1.W1Master;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class W1SensorsConfig {

    @Bean
    public W1Master getW1Master() {
        W1Master w1Master = new W1Master();
        w1Master.getDeviceIDs().forEach(id -> log.info("Discovered W1 device with ID: " + id));
        return w1Master;
    }
}
