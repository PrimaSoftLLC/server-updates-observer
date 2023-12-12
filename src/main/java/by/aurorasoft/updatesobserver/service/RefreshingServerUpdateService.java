package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.configuration.ServerUpdateFilePath;
import by.aurorasoft.updatesobserver.model.ServerUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static by.aurorasoft.updatesobserver.util.FileUtil.createFile;

@Service
public final class RefreshingServerUpdateService {
    private final ServerUpdateService updateService;
    private final ObjectMapper objectMapper;
    private final File updateFile;

    public RefreshingServerUpdateService(final ServerUpdateService updateService,
                                         final ObjectMapper objectMapper,
                                         final ServerUpdateFilePath filePath) {
        this.updateService = updateService;
        this.objectMapper = objectMapper;
        this.updateFile = createFile(filePath);
    }

    @Scheduled(fixedDelay = 300_000)
    public void refresh() {
        try {
            final Collection<ServerUpdate> updates = this.updateService.getAll();
            this.objectMapper.writeValue(this.updateFile, updates);
        } catch (final IOException cause) {
            throw new ServerUpdateRefreshingException(cause);
        }
    }

    static final class ServerUpdateRefreshingException extends RuntimeException {

        public ServerUpdateRefreshingException(final Exception cause) {
            super(cause);
        }
    }
}
