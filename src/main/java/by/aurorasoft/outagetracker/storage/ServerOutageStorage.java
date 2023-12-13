package by.aurorasoft.outagetracker.storage;

import by.aurorasoft.outagetracker.model.ServerOutage;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Storage for managing server outages with expiring entries.
 */
public final class ServerOutageStorage {
    private final ExpiringMap<String, ServerOutage> outageMap;

    public ServerOutageStorage(int maxSize, Collection<ServerOutage> backupServerOutage) {
        outageMap = createEmptyExpiringMap(maxSize);
        reloadAllAlive(backupServerOutage);
    }

    /**
     * Saves the server outage if it's still valid.
     *
     * @param serverOutage The server outage to save.
     */
    public void save(ServerOutage serverOutage) {
        String serverName = serverOutage.getServerName();
        serverOutage.findRemainingLifetimeInMillisIfAlive()
                .ifPresent(remainingLifetime ->
                        outageMap.put(serverName, serverOutage, remainingLifetime, TimeUnit.MILLISECONDS));
    }

    /**
     * Finds a server outage by server name.
     *
     * @return An Optional containing the ServerOutage.
     */
    public Optional<ServerOutage> findByServerName(String serverName) {
        return Optional.ofNullable(outageMap.get(serverName));
    }

    /**
     * Retrieves all server outages.
     *
     * @return A collection of ServerOutage objects.
     */
    public Collection<ServerOutage> findAll() {
        return outageMap.values();
    }

    /**
     * Removes a server outage by server name.
     *
     * @param serverName The name of the server.
     * @return An Optional containing the removed ServerOutage.
     */
    public Optional<ServerOutage> removeByServerName(String serverName) {
        return Optional.ofNullable(outageMap.remove(serverName));
    }

    private static ExpiringMap<String, ServerOutage> createEmptyExpiringMap(int maxSize) {
        return ExpiringMap.builder()
                .maxSize(maxSize)
                .expirationPolicy(ExpirationPolicy.CREATED)
                .variableExpiration()
                .build();
    }

    private void reloadAllAlive(Collection<ServerOutage> serverOutages) {
        serverOutages.forEach(this::save);
    }
}
