package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.InputStreamUtil.createObjectInputStream;
import static by.aurorasoft.updatesobserver.util.InputStreamUtil.readObjects;

@Component
public final class ServerUpdateLoader {
    private final String updateFilePath;

    public ServerUpdateLoader(@Value("${server-updates.file-path}") final String updateFilePath) {
        this.updateFilePath = updateFilePath;
    }

    public List<ServerUpdate> load() {
        try (final ObjectInputStream inputStream = createObjectInputStream(this.updateFilePath)) {
            return readObjects(inputStream, ServerUpdate.class);
        } catch (final IOException cause) {
            throw new ServerUpdateLoadingException(cause);
        }
    }

    static final class ServerUpdateLoadingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ServerUpdateLoadingException() {

        }

        @SuppressWarnings("unused")
        public ServerUpdateLoadingException(final String description) {
            super(description);
        }

        public ServerUpdateLoadingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ServerUpdateLoadingException(final String description, final Exception cause) {
            super(description, cause);
        }

    }
}
