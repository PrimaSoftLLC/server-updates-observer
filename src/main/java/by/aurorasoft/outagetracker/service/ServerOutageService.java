package by.aurorasoft.outagetracker.service;

import by.aurorasoft.outagetracker.model.ServerOutage;
import by.aurorasoft.outagetracker.storage.ServerOutageStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * Service class for managing server outages.
 */
@Service
@RequiredArgsConstructor
public final class ServerOutageService {
    private final ServerOutageStorage storage;

    /**
     * Saves a server outage serverOutage to the storage.
     */
    public void save(ServerOutage serverOutage) {
        storage.save(serverOutage);
    }

    /**
     * Retrieves the downtime of a specific server.
     *
     * @return An Optional containing the Instant of downtime if found.
     */
    public Optional<Instant> getDowntime(String serverName) {
        return storage.findByServerName(serverName)
                .map(ServerOutage::getDowntime);
    }

    /**
     * Retrieves all server outages.
     *
     * @return A collection of ServerOutage objects.
     */
    public Collection<ServerOutage> getAll() {
        return storage.findAll();
    }

    /**
     * Removes a server outage record based on the server name.
     *
     * @return An Optional containing the removed ServerOutage if found.
     */
    public Optional<ServerOutage> remove(String serverName) {
        return storage.removeByServerName(serverName);
    }
}
