package by.aurorasoft.updatesobserver.service.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static by.aurorasoft.updatesobserver.util.InstantUtil.plusMinutes;
import static java.time.Instant.now;

@Component
public final class ServerUpdateFactory {

    public ServerUpdate create(final String serverName, final long downtimeInMinutes, final long extraLifetimeInMinutes) {
        final Instant start = now();
        final Instant downtime = plusMinutes(start, downtimeInMinutes);
        final Instant lifetime = plusMinutes(downtime, extraLifetimeInMinutes);
        return new ServerUpdate(serverName, start, downtime, lifetime);
    }

}
