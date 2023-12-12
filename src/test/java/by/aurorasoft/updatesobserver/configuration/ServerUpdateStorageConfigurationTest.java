package by.aurorasoft.updatesobserver.configuration;

import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import by.aurorasoft.updatesobserver.storage.factory.ServerUpdateStorageFactory;
import org.junit.Test;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ServerUpdateStorageConfigurationTest {
    private final ServerUpdateStorageConfiguration configuration = new ServerUpdateStorageConfiguration();

    @Test
    public void storageShouldBeCreated() {
        ServerUpdateStorageFactory givenFactory = mock(ServerUpdateStorageFactory.class);

        ServerUpdateStorage givenStorage = new ServerUpdateStorage(MAX_VALUE, emptyList());
        when(givenFactory.create()).thenReturn(givenStorage);

        ServerUpdateStorage actual = configuration.serverUpdateStorage(givenFactory);
        assertSame(givenStorage, actual);
    }
}
