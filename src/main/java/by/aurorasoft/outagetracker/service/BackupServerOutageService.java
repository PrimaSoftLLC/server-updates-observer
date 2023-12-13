package by.aurorasoft.outagetracker.service;

import by.aurorasoft.outagetracker.configuration.ServerOutageFilePath;
import by.aurorasoft.outagetracker.model.ServerOutage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static by.aurorasoft.outagetracker.util.FileUtil.createFile;

@Service
public final class BackupServerOutageService {
    private final ServerOutageService service;
    private final ObjectMapper objectMapper;
    private final File updateFile;


    public BackupServerOutageService(ServerOutageService service,
                                     ObjectMapper objectMapper,
                                     ServerOutageFilePath filePath) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.updateFile = createFile(filePath);
    }

    /**
     * Periodically backup the server outage data.
     */
    @Scheduled(fixedDelay = 300_000)
    public void backup() {
        try {
            Collection<ServerOutage> updates = service.getAll();
            objectMapper.writeValue(updateFile, updates);
        } catch (IOException cause) {
            throw new ServerOutageBackupException(cause);
        }
    }

    /**
     * Custom exception for handling errors during the backup process.
     */
    static final class ServerOutageBackupException extends RuntimeException {

        public ServerOutageBackupException(Exception cause) {
            super(cause);
        }
    }
}
