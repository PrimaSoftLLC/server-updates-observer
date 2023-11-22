package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.configuration.property.ServerUpdateFilePath;
import by.aurorasoft.updatesobserver.model.ServerUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static by.aurorasoft.updatesobserver.util.FileUtil.createFile;

@Service
public final class ServerUpdateRefreshingService {
    private final ServerUpdateService updateService;
    private final ObjectMapper objectMapper;
    private final File updateFile;

    public ServerUpdateRefreshingService(final ServerUpdateService updateService,
                                         final ObjectMapper objectMapper,
                                         final ServerUpdateFilePath filePath) {
        this.updateService = updateService;
        this.objectMapper = objectMapper;
        this.updateFile = createFile(filePath);
    }

    @Scheduled(cron = "0 */1 * ? * *")
    public void refresh() {
        try {
            final Collection<ServerUpdate> updates = this.updateService.findAll();
            this.objectMapper.writeValue(this.updateFile, updates);
        } catch (final IOException cause) {
            throw new ServerUpdateRefreshingException(cause);
        }
    }

    static final class ServerUpdateRefreshingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ServerUpdateRefreshingException() {

        }

        @SuppressWarnings("unused")
        public ServerUpdateRefreshingException(final String description) {
            super(description);
        }

        public ServerUpdateRefreshingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ServerUpdateRefreshingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
