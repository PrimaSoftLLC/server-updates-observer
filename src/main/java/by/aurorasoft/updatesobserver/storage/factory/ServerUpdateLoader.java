package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.configuration.property.ServerUpdateFilePath;
import by.aurorasoft.updatesobserver.model.ServerUpdate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.FileUtil.createFile;
import static by.aurorasoft.updatesobserver.util.FileUtil.isEmpty;
import static java.util.Collections.emptyList;

@Component
public final class ServerUpdateLoader {
    private static final TypeReference<List<ServerUpdate>> SERVER_UPDATES_TYPE_REFERENCE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;
    private final File updateFile;

    public ServerUpdateLoader(final ObjectMapper objectMapper, final ServerUpdateFilePath serverUpdateFilePath) {
        this.objectMapper = objectMapper;
        this.updateFile = createFile(serverUpdateFilePath);
    }

    public Collection<ServerUpdate> load() {
        return !isEmpty(this.updateFile) ? this.loadUpdatesFromFile() : emptyList();
    }

    private Collection<ServerUpdate> loadUpdatesFromFile() {
        try {
            return this.objectMapper.readValue(this.updateFile, SERVER_UPDATES_TYPE_REFERENCE);
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
