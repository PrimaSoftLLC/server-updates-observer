package by.aurorasoft.outagetracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.OptionalLong;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.OptionalLong.empty;

/**
 * Represents information about a server outage, including the server's name, the start of the downtime,
 * and the expected lifetime of the outage. This class is used to track and calculate the remaining
 * lifetime of the outage if the server is still down.
 */
@Value
@Builder
@AllArgsConstructor
@SuppressWarnings("ClassCanBeRecord")
public class ServerOutage implements Serializable {

    /**
     * The name of the server experiencing the outage.
     */
    String serverName;

    /**
     * The instant when the downtime begins.
     */
    Instant downtime;

    /**
     * The instant until which the downtime is expected to last.
     * It is assumed that lifetime is always greater than the downtime.
     */
    Instant lifetime;

    /**
     * Calculates the remaining lifetime of the outage in milliseconds, if the server is still down.
     *
     * @return an OptionalLong containing the remaining lifetime in milliseconds if it is greater than zero,
     *         or an empty OptionalLong if the server is no longer in an outage.
     */
    public OptionalLong findRemainingLifetimeInMillisIfAlive() {
        final long remainingLifetimeInMillis = this.findRemainingLifetimeInMillis();
        return remainingLifetimeInMillis > 0 ? OptionalLong.of(remainingLifetimeInMillis) : empty();
    }

    /**
     * Helper method to compute the remaining lifetime of the outage.
     *
     * @return the remaining lifetime in milliseconds.
     */
    private long findRemainingLifetimeInMillis() {
        final Instant now = now();
        final Duration lifetimeDuration = between(now, this.lifetime);
        return lifetimeDuration.toMillis();
    }
}
