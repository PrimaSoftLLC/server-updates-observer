package by.aurorasoft.updatesobserver.service.savingupdate;


import by.aurorasoft.updatesobserver.model.ServerUpdate;

import java.io.ObjectOutputStream;
import java.util.Collection;

import static by.aurorasoft.updatesobserver.util.OutputStreamUtil.*;

public final class ServerUpdateSerializer implements AutoCloseable {
    private final ObjectOutputStream outputStream;

    public ServerUpdateSerializer(final String filePath) {
        this.outputStream = createObjectOutputStream(filePath);
    }

    public void serialize(final Collection<ServerUpdate> updates) {
        writeObjects(this.outputStream, updates);
    }

    @Override
    public void close() {
        closeStream(this.outputStream);
    }
}
