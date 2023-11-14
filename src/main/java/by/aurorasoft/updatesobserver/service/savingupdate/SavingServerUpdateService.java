package by.aurorasoft.updatesobserver.service.savingupdate;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.service.ServerUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
        try (final ServerUpdateSerializer serializer = new ServerUpdateSerializer(this.updateFilePath)) {
            final Collection<ServerUpdate> updates = this.updateService.findAll();
            serializer.serialize(updates);
        }
    }
}
