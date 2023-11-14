package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.service.ServerUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;

import static by.aurorasoft.updatesobserver.util.OutputStreamUtil.createObjectOutputStream;
import static by.aurorasoft.updatesobserver.util.OutputStreamUtil.writeObjects;

@Service
public final class SavingServerUpdateService {
    private final String updateFilePath;
    private final ServerUpdateService updateService;

    public SavingServerUpdateService(@Value("${server-updates.file-path}") final String updateFilePath,
                                     final ServerUpdateService updateService) {
        this.updateFilePath = updateFilePath;
        this.updateService = updateService;
    }

    @EventListener(ContextClosedEvent.class)
    public void saveUpdates() {
        try (final ObjectOutputStream outputStream = createObjectOutputStream(this.updateFilePath)) {
            final Collection<ServerUpdate> updates = this.updateService.findAll();
            writeObjects(outputStream, updates);
        } catch (final IOException cause) {
            throw new SavingServerUpdateException(cause);
        }
    }

    static final class SavingServerUpdateException extends RuntimeException {

        @SuppressWarnings("unused")
        public SavingServerUpdateException() {

        }

        @SuppressWarnings("unused")
        public SavingServerUpdateException(final String description) {
            super(description);
        }

        public SavingServerUpdateException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public SavingServerUpdateException(final String description, final Exception cause) {
            super(description, cause);
        }

    }
}
