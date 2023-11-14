package by.aurorasoft.updatesobserver.service.savingupdate;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public final class SavingServerUpdateService {
    private final ServerUpdateStorage updateStorage;

    @EventListener(ContextClosedEvent.class)
    public void saveUpdates() {
        final Collection<ServerUpdate> updates = this.updateStorage.findAll();

    }
}
