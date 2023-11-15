package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
public final class ServerUpdateService {
    private final ServerUpdateStorage updateStorage;

    public void save(final ServerUpdate update) {
        this.updateStorage.save(update);
    }

    public Optional<Instant> findUpdateDowntime(final String serverName) {
        return this.updateStorage.findByServerName(serverName)
                .filter(ServerUpdateService::isAlive)
                .map(ServerUpdateService::findDowntime);
    }

    public Collection<ServerUpdate> findAll() {
        return this.updateStorage.findAll();
    }

    private static boolean isAlive(final ServerUpdate update) {
        final Instant updateStart = update.getStart();
        final Duration lifeDuration = findLifeDuration(update);
        final Instant endLifetime = updateStart.plus(lifeDuration);
        final Instant now = now();
        return now.isBefore(endLifetime);
    }

    //TODO: refactor
    private static Duration findLifeDuration(final ServerUpdate update) {
        final int downtimeInMinutes = update.getDowntimeInMinutes();
        final int lifetimeInMinutes = update.getExtraDowntimeInMinutes();
        return Duration.of(downtimeInMinutes + lifetimeInMinutes, MINUTES);
    }

    private static Instant findDowntime(final ServerUpdate update) {
        final Instant start = update.getStart();
        final Duration downtimeDuration = findDowntimeDuration(update);
        return start.plus(downtimeDuration);
    }

    //TODO: refactor
    private static Duration findDowntimeDuration(final ServerUpdate update) {
        final int downtimeInMinutes = update.getDowntimeInMinutes();
        return Duration.of(downtimeInMinutes, MINUTES);
    }
}
