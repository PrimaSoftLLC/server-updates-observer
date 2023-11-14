package by.aurorasoft.updatesobserver.service.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static java.time.Instant.now;

@Component
public final class ServerUpdateFactory {

    //TODO: validate downtimeInMinutes <= lifetimeInMinutes
    public ServerUpdate create(final String serverName, final int downtimeInMinutes, final int lifetimeInMinutes) {
        final Instant start = now();
        return new ServerUpdate(serverName, start, downtimeInMinutes, lifetimeInMinutes);
    }
}
