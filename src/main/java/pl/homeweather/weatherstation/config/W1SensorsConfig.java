package pl.homeweather.weatherstation.config;

import com.pi4j.io.w1.W1Master;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class W1SensorsConfig {

    @Bean
    public W1Master getW1Master() {
        return new W1Master();
    }
}
