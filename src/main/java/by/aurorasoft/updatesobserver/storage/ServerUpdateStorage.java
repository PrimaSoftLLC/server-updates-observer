package by.aurorasoft.updatesobserver.storage;

import by.aurorasoft.updatesobserver.model.ServerUpdate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public final class ServerUpdateStorage {
    private final Map<String, ServerUpdate> updatesByServerNames;

    public ServerUpdateStorage(final List<ServerUpdate> updates) {
        this.updatesByServerNames = updates.stream()
                .collect(
                        toMap(
                                ServerUpdate::getServerName,
                                identity()
                        )
                );
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
}
