package by.aurorasoft.updatesobserver.model;

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

@Value
@AllArgsConstructor
@Builder
public class ServerUpdate implements Serializable {
    String serverName;
    Instant downtime;
    Instant lifetime;

    public OptionalLong findRemainingLifetimeInMillisIfAlive() {
        final long remainingLifetimeInMillis = this.findRemainingLifetimeInMillis();
        return remainingLifetimeInMillis > 0 ? OptionalLong.of(remainingLifetimeInMillis) : empty();
    }

    private long findRemainingLifetimeInMillis() {
        final Instant now = now();
        final Duration lifetimeDuration = between(now, this.lifetime);
        return lifetimeDuration.toMillis();
    }
}
