package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class ServerUpdateStorageFactory {
    private final ServerUpdateLoader updateLoader;
    private final int storageMaxSize;

    public ServerUpdateStorageFactory(final ServerUpdateLoader updateLoader,
                                      @Value("${server-updates.storage.max-size}") final int storageMaxSize) {
        this.updateLoader = updateLoader;
        this.storageMaxSize = storageMaxSize;
    }

    public ServerUpdateStorage create() {
        final List<ServerUpdate> updates = this.updateLoader.load();
        return new ServerUpdateStorage(this.storageMaxSize, updates);
    }

}
