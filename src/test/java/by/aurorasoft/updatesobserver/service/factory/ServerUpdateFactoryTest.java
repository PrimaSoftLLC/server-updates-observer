package by.aurorasoft.updatesobserver.service.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.junit.Test;

import java.time.Instant;

import static java.time.Duration.between;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class ServerUpdateFactoryTest {
    private final ServerUpdateFactory factory = new ServerUpdateFactory();

    @Test
    public void updateShouldBeCreated() {
        final String givenServerName = "server";
        final long givenDowntimeInMinutes = 5;
        final long givenExtraLifetimeInMinutes = 3;

        final ServerUpdate actual = this.factory.create(
                givenServerName,
                givenDowntimeInMinutes,
                givenExtraLifetimeInMinutes
        );

        final String actualServerName = actual.getServerName();
        assertEquals(givenServerName, actualServerName);

        final Instant downtime = actual.getDowntime();
        assertNotNull(downtime);

        final Instant lifetime = actual.getLifetime();
        assertNotNull(lifetime);

        final long actualMinuteDifferenceDowntimeAndLifetime = between(downtime, lifetime).toMinutes();
        assertEquals(givenExtraLifetimeInMinutes, actualMinuteDifferenceDowntimeAndLifetime);
    }
}
