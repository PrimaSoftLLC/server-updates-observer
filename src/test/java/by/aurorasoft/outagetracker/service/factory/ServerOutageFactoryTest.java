package by.aurorasoft.outagetracker.service.factory;

import by.aurorasoft.outagetracker.model.ServerOutage;
import org.junit.Test;

import static java.time.Duration.between;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class ServerOutageFactoryTest {
    private final ServerOutageFactory factory = new ServerOutageFactory();

    @Test
    public void updateShouldBeCreated() {
        String givenServerName = "server";
        long givenDowntimeMinutes = 5;
        long givenExtraLifetimeInMinutes = 3;

        ServerOutage actual = factory.create(givenServerName, givenDowntimeMinutes, givenExtraLifetimeInMinutes);

        assertEquals(givenServerName, actual.getServerName());
        assertNotNull(actual.getDowntime());
        assertNotNull(actual.getLifetime());

        long actualMinuteDifferenceDowntimeAndLifetime = between(actual.getDowntime(), actual.getLifetime()).toMinutes();
        assertEquals(givenExtraLifetimeInMinutes, actualMinuteDifferenceDowntimeAndLifetime);
    }
}
