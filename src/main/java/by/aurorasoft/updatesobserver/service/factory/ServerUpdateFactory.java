package by.aurorasoft.updatesobserver.service.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static by.aurorasoft.updatesobserver.util.InstantUtil.plusMinutes;
import static java.time.Instant.now;

@Component
public final class ServerUpdateFactory {

    public ServerUpdate create(String serverName, long downtimeInMinutes, long extraLifetimeInMinutes) {
        final Instant now = now();
        final Instant downtime = plusMinutes(now, downtimeInMinutes);
        final Instant lifetime = plusMinutes(downtime, extraLifetimeInMinutes);
        return new ServerUpdate(serverName, downtime, lifetime);
    }

}
