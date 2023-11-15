package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
public final class ServerUpdateService {
    private final ServerUpdateStorage updateStorage;

    public void save(final ServerUpdate update) {
        this.updateStorage.save(update);
    }

    public Optional<Instant> findUpdateDowntime(final String serverName) {
        final Optional<ServerUpdate> optionalUpdate = this.updateStorage.findByServerName(serverName);
        return optionalUpdate.map(ServerUpdateService::findDowntime);
    }

    public Collection<ServerUpdate> findAll() {
        return this.updateStorage.findAll();
    }

    private static Instant findDowntime(final ServerUpdate update) {
        final Instant start = update.getStart();
        final Duration downtimeDuration = findDowntimeDuration(update);
        return start.plus(downtimeDuration);
    }

    private static Duration findDowntimeDuration(final ServerUpdate update) {
        final int downtimeInMinutes = update.getDowntimeInMinutes();
        return Duration.of(downtimeInMinutes, MINUTES);
    }
}
