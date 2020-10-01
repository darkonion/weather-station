package pl.homeweather.weatherstation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.homeweather.weatherstation.drivers.PMS7003Driver;
import pl.homeweather.weatherstation.dtos.AirPurityMeasurement;

import java.time.Duration;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Slf4j
@Service
public class PMS7003Service {

    private final PMS7003Driver driver = new PMS7003Driver();
    private final ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor();


    public AirPurityMeasurement getMeasurement() {
        AirPurityMeasurement measurement = AirPurityMeasurement.builder().build();
        if (!driver.activate()) {
            log.error("Unable to activate driver");
        }

        ScheduledFuture<AirPurityMeasurement> future = scheduler.schedule(
                driver::measure,
                Duration.ofMillis(40000L).toMillis(),
                TimeUnit.MILLISECONDS);

        try {
            measurement = future.get(
                    Duration.ofMinutes(1L).toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Measurement interrupted: {}", e.getMessage());
            future.cancel(true);
        } catch (TimeoutException e) {
            log.error("Measurement timed out: {}", e.getMessage());
            future.cancel(true);
        } catch (ExecutionException e) {
            log.error("Measurement failed: {}", e.getMessage());
            future.cancel(true);
        } finally {
            driver.deactivate();
        }
        log.info(measurement.toString());
        return measurement;
    }
}
