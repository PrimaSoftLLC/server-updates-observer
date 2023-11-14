package by.aurorasoft.updatesobserver.service.savingupdate;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.service.ServerUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public final class SavingServerUpdateService {
    private final ServerUpdateService updateService;

    @EventListener(ContextClosedEvent.class)
    public void saveUpdates() {
        final Collection<ServerUpdate> updates = this.updateService.findAll();

    }
}
