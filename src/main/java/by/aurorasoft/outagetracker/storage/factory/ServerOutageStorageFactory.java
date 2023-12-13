package by.aurorasoft.outagetracker.storage.factory;

import by.aurorasoft.outagetracker.model.ServerOutage;
import by.aurorasoft.outagetracker.storage.ServerOutageStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Factory for creating instances of ServerOutageStorage.
 */
@Component
public final class ServerOutageStorageFactory {
    private final ServerOutageBackupRestorer restorer;
    private final int storageMaxSize;

    public ServerOutageStorageFactory(ServerOutageBackupRestorer restorer,
                                      @Value("${server-updates.storage.max-size}") int storageMaxSize) {
        this.restorer = restorer;
        this.storageMaxSize = storageMaxSize;
    }

    /**
     * Creates an instance of ServerOutageStorage preloaded with server outage data.
     *
     * @return A new instance of ServerOutageStorage.
     */
    public ServerOutageStorage create() {
        Collection<ServerOutage> serverOutages = restorer.restore();
        return new ServerOutageStorage(storageMaxSize, serverOutages);
    }
}
