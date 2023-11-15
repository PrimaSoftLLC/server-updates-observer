package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class ServerUpdateService {
    private final ServerUpdateStorage updateStorage;

    public void save(final ServerUpdate update) {
        this.updateStorage.save(update);
    }

    public Optional<Instant> findUpdateDowntime(final String serverName) {
        final Optional<ServerUpdate> optionalUpdate = this.updateStorage.findByServerName(serverName);
        return optionalUpdate.map(ServerUpdate::getDowntime);
    }

    public Collection<ServerUpdate> findAll() {
        return this.updateStorage.findAll();
    }
}
