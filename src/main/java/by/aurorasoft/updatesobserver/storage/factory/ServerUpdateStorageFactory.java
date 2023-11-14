package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ServerUpdateStorageFactory {
    private final ServerUpdateLoader updateLoader;

    public ServerUpdateStorage create() {
        final List<ServerUpdate> updates = this.updateLoader.load();
        return new ServerUpdateStorage(updates);
    }

}
