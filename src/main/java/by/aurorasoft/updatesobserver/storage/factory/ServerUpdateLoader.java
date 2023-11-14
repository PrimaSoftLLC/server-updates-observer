package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class ServerUpdateLoader {
    private final String updateFilePath;

    public ServerUpdateLoader(@Value("${server-updates.file-path}") final String updateFilePath) {
        this.updateFilePath = updateFilePath;
    }

    public List<ServerUpdate> load() {
        try (final ServerUpdateDeserializer deserializer = new ServerUpdateDeserializer(this.updateFilePath)) {
            return deserializer.deserialize();
        }
    }
}
