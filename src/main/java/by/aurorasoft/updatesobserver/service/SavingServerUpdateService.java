package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Service
public final class SavingServerUpdateService {
    private final ServerUpdateService updateService;
    private final ObjectMapper objectMapper;
    private final File updateFile;

    public SavingServerUpdateService(final ServerUpdateService updateService,
                                     final ObjectMapper objectMapper,
                                     @Value("${server-updates.file-path}") final String updateFilePath) {
        this.updateService = updateService;
        this.objectMapper = objectMapper;
        this.updateFile = new File(updateFilePath);
    }

    @EventListener(ContextClosedEvent.class)
    public void saveUpdates() {
        try {
            final Collection<ServerUpdate> updates = this.updateService.findAll();
            this.objectMapper.writeValue(this.updateFile, updates);
        } catch (final IOException cause) {
            throw new ServerUpdateSavingException(cause);
        }
    }

    static final class ServerUpdateSavingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ServerUpdateSavingException() {

        }

        @SuppressWarnings("unused")
        public ServerUpdateSavingException(final String description) {
            super(description);
        }

        public ServerUpdateSavingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ServerUpdateSavingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
