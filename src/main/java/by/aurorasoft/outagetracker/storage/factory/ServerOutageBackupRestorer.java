package by.aurorasoft.outagetracker.storage.factory;

import by.aurorasoft.outagetracker.configuration.ServerOutageFilePath;
import by.aurorasoft.outagetracker.model.ServerOutage;
import by.aurorasoft.outagetracker.util.FileUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static by.aurorasoft.outagetracker.util.FileUtil.createFile;
import static java.util.Collections.emptyList;

/**
 * Loader for server outage data from a file.
 */
@Component
public final class ServerOutageBackupRestorer {
    private static final TypeReference<Collection<ServerOutage>> SERVER_OUTAGE_COLLECTION_TYPE_REFERENCE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;
    private final File backupFile;

    public ServerOutageBackupRestorer(ObjectMapper objectMapper, ServerOutageFilePath serverOutageFilePath) {
        this.objectMapper = objectMapper;
        this.backupFile = createFile(serverOutageFilePath);
    }

    /**
     * restore server outage data from a backup file.
     *
     * @return A collection of ServerOutage objects.
     */
    public Collection<ServerOutage> restore() {
        if (backupFile.exists() && backupFile.canRead() && !FileUtil.isEmpty(backupFile)) {
            return restoreFromFile();
        }
        return emptyList();
    }

    private Collection<ServerOutage> restoreFromFile() {
        try {
            return objectMapper.readValue(backupFile, SERVER_OUTAGE_COLLECTION_TYPE_REFERENCE);
        } catch (IOException cause) {
            throw new ServerOutageLoadingException(cause);
        }
    }

    static final class ServerOutageLoadingException extends RuntimeException {
        public ServerOutageLoadingException(Exception cause) {
            super(cause);
        }
    }
}
