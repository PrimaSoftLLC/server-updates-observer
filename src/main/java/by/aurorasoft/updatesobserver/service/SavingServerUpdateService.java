package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static by.aurorasoft.updatesobserver.util.SerializationUtil.writeObjects;

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
        final Collection<ServerUpdate> updates = this.updateService.findAll();
        writeObjects(this.updateFilePath, updates);
    }
}
