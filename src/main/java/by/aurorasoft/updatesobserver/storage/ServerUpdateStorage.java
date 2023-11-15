package by.aurorasoft.updatesobserver.storage;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Collection;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static net.jodah.expiringmap.ExpirationPolicy.CREATED;

public final class ServerUpdateStorage {
    private final ExpiringMap<String, ServerUpdate> updatesByServerNames;

    public ServerUpdateStorage(final int maxSize, final Collection<ServerUpdate> reloadedUpdates) {
        this.updatesByServerNames = createEmptyExpiringMap(maxSize);
        this.reloadAllAlive(reloadedUpdates);
    }

    public void save(final ServerUpdate update) {
        final String serverName = update.getServerName();
        final long remainingLifetimeInMillis = update.findRemainingLifetimeInMillis();
        this.updatesByServerNames.put(serverName, update, remainingLifetimeInMillis, MILLISECONDS);
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
                .expirationPolicy(CREATED)
                .variableExpiration()
                .build();
    }

    private void reloadAllAlive(final Collection<ServerUpdate> updates) {
        updates.stream()
                .filter(ServerUpdate::isAlive)
                .forEach(this::save);
    }
}
