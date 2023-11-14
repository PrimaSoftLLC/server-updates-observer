package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.aurorasoft.updatesobserver.util.DeserializationUtil.readObjects;

@Component
public final class ServerUpdateLoader {
    private final String updateFilePath;

    public ServerUpdateLoader(@Value("${server-updates.file-path}") final String updateFilePath) {
        this.updateFilePath = updateFilePath;
    }

    public List<ServerUpdate> load() {
        return readObjects(this.updateFilePath, ServerUpdate.class);
    }
}
