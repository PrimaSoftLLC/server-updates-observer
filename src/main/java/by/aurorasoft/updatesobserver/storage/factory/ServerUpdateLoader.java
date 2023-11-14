package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class ServerUpdateLoader {
    private final String filePath;

    public ServerUpdateLoader(@Value("${server-updates.file-path}") final String filePath) {
        this.filePath = filePath;
    }

    public List<ServerUpdate> load() {
        try (final ServerUpdateDeserializer deserializer = new ServerUpdateDeserializer(this.filePath)) {
            return deserializer.deserialize();
        }
    }
}
