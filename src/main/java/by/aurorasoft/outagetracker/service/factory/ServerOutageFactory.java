package by.aurorasoft.outagetracker.service.factory;

import by.aurorasoft.outagetracker.model.ServerOutage;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static by.aurorasoft.outagetracker.util.InstantUtil.plusMinutes;
import static java.time.Instant.now;

@Component
public final class ServerOutageFactory {

    /**
     * Creates a new ServerOutage instance with specified parameters.
     * @return A new ServerOutage instance.
     */
    public ServerOutage create(String serverName, long downtimeMinutes, long extraLifetimeMinutes) {
        validateParameters(downtimeMinutes, extraLifetimeMinutes);

        Instant downtime = plusMinutes(now(), downtimeMinutes);
        Instant lifetime = plusMinutes(downtime, extraLifetimeMinutes);
        return new ServerOutage(serverName, downtime, lifetime);
    }

    /**
     * Validates the downtime and extra lifetime parameters.
     */
    private void validateParameters(long downtimeMinutes, long extraLifetimeMinutes) {
        if (downtimeMinutes <= 0) {
            throw new IllegalArgumentException("Downtime must be greater than 0");
        }
        if (extraLifetimeMinutes <= 0) {
            throw new IllegalArgumentException("Extra lifetime must be greater than 0");
        }
    }
}
