package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;

import java.io.ObjectInputStream;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.InputStreamUtil.*;

public final class ServerUpdateDeserializer implements AutoCloseable {
    private final ObjectInputStream inputStream;

    public ServerUpdateDeserializer(final String filePath) {
        this.inputStream = createObjectInputStream(filePath);
    }

    public List<ServerUpdate> deserialize() {
        return readObjects(this.inputStream, ServerUpdate.class);
    }

    @Override
    public void close() {
        closeStream(this.inputStream);
    }
}
