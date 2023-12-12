package by.aurorasoft.updatesobserver.storage;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

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
        return this.extractByServerName(serverName, Map::get);
    }

    public Collection<ServerUpdate> findAll() {
        return this.updatesByServerNames.values();
    }

    public Optional<ServerUpdate> removeByServerName(final String serverName) {
        return this.extractByServerName(serverName, Map::remove);
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

    private Optional<ServerUpdate> extractByServerName(final String serverName,
                                                       final BiFunction<Map<String, ServerUpdate>, String, ServerUpdate> extractor) {
        final ServerUpdate update = extractor.apply(this.updatesByServerNames, serverName);
        return ofNullable(update);
    }
}
