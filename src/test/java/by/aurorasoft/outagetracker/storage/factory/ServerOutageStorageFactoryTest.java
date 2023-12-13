package by.aurorasoft.outagetracker.storage.factory;

import by.aurorasoft.outagetracker.model.ServerOutage;
import by.aurorasoft.outagetracker.storage.ServerOutageStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.OptionalLong;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class ServerOutageStorageFactoryTest {

    private static final int GIVEN_STORAGE_MAX_SIZE = 10;
    private static final long REMAINING_LIFETIME_IN_MILLIS = 500;

    @Mock
    private ServerOutageBackupRestorer mockedRestorer;

    private ServerOutageStorageFactory factory;

    @Before
    public void initializeUpdateStorageFactory() {
        factory = new ServerOutageStorageFactory(mockedRestorer, GIVEN_STORAGE_MAX_SIZE);
    }

    @Test
    public void shouldCreateStorageWithRestoredServerOutages() {
        ServerOutage firstServerOutage = createMockedServerOutage("first-server");
        ServerOutage secondServerOutage = createMockedServerOutage("second-server");

        List<ServerOutage> restoredOutages = List.of(firstServerOutage, secondServerOutage);
        when(mockedRestorer.restore()).thenReturn(restoredOutages);

        ServerOutageStorage actualStorage = factory.create();

        assertServerOutageStored(actualStorage, firstServerOutage);
        assertServerOutageStored(actualStorage, secondServerOutage);
    }

    private static ServerOutage createMockedServerOutage(String serverName) {
        ServerOutage serverOutage = mock(ServerOutage.class);
        when(serverOutage.getServerName()).thenReturn(serverName);
        when(serverOutage.findRemainingLifetimeInMillisIfAlive())
                .thenReturn(OptionalLong.of(REMAINING_LIFETIME_IN_MILLIS));
        return serverOutage;
    }

    private static void assertServerOutageStored(ServerOutageStorage storage, ServerOutage expectedOutage) {
        assertEquals(
                expectedOutage,
                storage.findByServerName(expectedOutage.getServerName())
                .orElseThrow(() -> new AssertionError("Server outage for " + expectedOutage.getServerName() + " not stored")));
    }
}
