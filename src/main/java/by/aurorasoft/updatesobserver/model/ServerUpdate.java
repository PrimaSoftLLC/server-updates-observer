package by.aurorasoft.updatesobserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

import static java.time.Duration.between;
import static java.time.Instant.now;

@Value
@AllArgsConstructor
@Builder
public class ServerUpdate implements Serializable {
    String serverName;
    Instant downtime;
    Instant lifetime;

    public boolean isAlive() {
        final Instant now = now();
        return !now.isAfter(this.lifetime);
    }

    public long findRemainingLifetimeInMillis() {
        final Instant now = now();
        return between(now, this.lifetime).toMillis();
    }
}
