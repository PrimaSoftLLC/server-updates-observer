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

    public void put(ServerUpdate update) {
        updateStorage.saveIfAlive(update);
    }

    public Optional<Instant> get(String serverName) {
        return updateStorage.findByServerName(serverName)
                .map(ServerUpdate::getDowntime);
    }

    public Collection<ServerUpdate> getAll() {
        return updateStorage.findAll();
    }

    public Optional<ServerUpdate> remove(String serverName) {
        return updateStorage.removeByServerName(serverName);
    }
}
