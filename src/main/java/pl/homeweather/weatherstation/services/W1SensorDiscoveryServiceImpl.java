package pl.homeweather.weatherstation.services;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
@Service
public class W1SensorDiscoveryServiceImpl implements W1SensorDiscoveryService {

    private final W1Master w1Master;

    public W1SensorDiscoveryServiceImpl(W1Master w1Master) {
        this.w1Master = w1Master;
    }

    @Override
    public Optional<TemperatureSensor> getTemperatureSensor(int maxRetryAmount) {
        int currentRetryCount = 1;
        while (true) {
            try {
                return of(w1Master.getDevices(TemperatureSensor.class).get(0));
            } catch (IndexOutOfBoundsException e) {
                log.info("Discovering temperature sensor, attempt: " + currentRetryCount);
                if (currentRetryCount >= maxRetryAmount) {
                    log.info("Temperature sensor currently unavailable!");
                    return empty();
                }
                currentRetryCount++;
                waitForComponent();
            }
        }
    }

    private void waitForComponent() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
