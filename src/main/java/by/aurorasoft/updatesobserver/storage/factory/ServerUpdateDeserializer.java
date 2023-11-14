package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.InputStreamUtil.createObjectInputStream;
import static by.aurorasoft.updatesobserver.util.InputStreamUtil.readObjects;

@Component
public final class ServerUpdateDeserializer {

    public List<ServerUpdate> deserialize(final String filePath) {
        try (final ObjectInputStream inputStream = createObjectInputStream(filePath)) {
            return readObjects(inputStream, ServerUpdate.class);
        } catch (final IOException cause) {
            throw new ServerUpdateDeserializationException(cause);
        }
    }

    static final class ServerUpdateDeserializationException extends RuntimeException {

        @SuppressWarnings("unused")
        public ServerUpdateDeserializationException() {

        }

        @SuppressWarnings("unused")
        public ServerUpdateDeserializationException(final String description) {
            super(description);
        }

        public ServerUpdateDeserializationException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ServerUpdateDeserializationException(final String description, final Exception cause) {
            super(description, cause);
        }

    }
}
