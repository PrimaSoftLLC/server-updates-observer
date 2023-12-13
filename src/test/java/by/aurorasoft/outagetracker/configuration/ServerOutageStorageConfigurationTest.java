package by.aurorasoft.outagetracker.configuration;

import by.aurorasoft.outagetracker.storage.ServerOutageStorage;
import by.aurorasoft.outagetracker.storage.factory.ServerOutageStorageFactory;
import org.junit.Test;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ServerOutageStorageConfigurationTest {
    private final ServerOutageStorageConfiguration configuration = new ServerOutageStorageConfiguration();

    @Test
    public void storageShouldBeCreated() {
        ServerOutageStorageFactory givenFactory = mock(ServerOutageStorageFactory.class);

        ServerOutageStorage givenStorage = new ServerOutageStorage(MAX_VALUE, emptyList());
        when(givenFactory.create()).thenReturn(givenStorage);

        ServerOutageStorage actual = configuration.serverOutageStorage(givenFactory);
        assertSame(givenStorage, actual);
    }
}
