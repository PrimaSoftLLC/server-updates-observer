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

    public void saveIfAlive(final ServerUpdate update) {
        final String serverName = update.getServerName();
        update.findRemainingLifetimeInMillisIfAlive()
                .ifPresent(
                        remainingLifetimeInMillis -> this.updatesByServerNames.put(
                                serverName,
                                update,
                                remainingLifetimeInMillis,
                                MILLISECONDS
                        )
                );
    }

    public Optional<ServerUpdate> findByServerName(final String serverName) {
        final ServerUpdate update = this.updatesByServerNames.get(serverName);
        return ofNullable(update);
    }

    public Collection<ServerUpdate> findAll() {
        return this.updatesByServerNames.values();
    }

    public void removeByServerName(final String serverName) {
        this.updatesByServerNames.remove(serverName);
    }

    private static ExpiringMap<String, ServerUpdate> createEmptyExpiringMap(final int maxSize) {
        return ExpiringMap.builder()
                .maxSize(maxSize)
                .expirationPolicy(CREATED)
                .variableExpiration()
                .build();
    }

    private void reloadAllAlive(final Collection<ServerUpdate> updates) {
        updates.forEach(this::saveIfAlive);
    }
}
