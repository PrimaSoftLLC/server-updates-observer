package by.aurorasoft.updatesobserver.storage;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import net.jodah.expiringmap.ExpiringMap;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static java.time.Instant.now;
import static java.util.Optional.ofNullable;

public final class ServerUpdateStorage {
    private final ExpiringMap<String, ServerUpdate> updatesByServerNames;

    public ServerUpdateStorage(final int maxSize, final Collection<ServerUpdate> updates) {
        this.updatesByServerNames = createEmptyExpiringMap(maxSize);
        putAll(this.updatesByServerNames, updates);
    }

    public void save(final ServerUpdate update) {
        final String serverName = update.getServerName();
        this.updatesByServerNames.put(serverName, update);
    }

    public Optional<ServerUpdate> findByServerName(final String serverName) {
        final ServerUpdate update = this.updatesByServerNames.get(serverName);
        return ofNullable(update);
    }

    public Collection<ServerUpdate> findAll() {
        return this.updatesByServerNames.values();
    }

    private static ExpiringMap<String, ServerUpdate> createEmptyExpiringMap(final int maxSize) {
        return ExpiringMap.builder()
                .maxSize(maxSize)
                .build();
    }

    private static void putAll(final ExpiringMap<String, ServerUpdate> updatesByServerNames,
                               final Collection<ServerUpdate> updates) {
        updatesByServerNames.put()
    }

    private static boolean isNotExpired(final ServerUpdate update) {
        final Instant start = update.getStart();
        final Instant now = now();

    }

    private static void put(final ExpiringMap<String, ServerUpdate> updatesByServerNames, final ServerUpdate update) {

    }
}
