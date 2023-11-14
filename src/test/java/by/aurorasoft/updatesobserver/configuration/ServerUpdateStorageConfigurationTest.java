package by.aurorasoft.updatesobserver.configuration;

import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import by.aurorasoft.updatesobserver.storage.factory.ServerUpdateStorageFactory;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ServerUpdateStorageConfigurationTest {
    private final ServerUpdateStorageConfiguration configuration = new ServerUpdateStorageConfiguration();

    @Test
    public void storageShouldBeCreated() {
        final ServerUpdateStorageFactory givenFactory = mock(ServerUpdateStorageFactory.class);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(emptyList());
        when(givenFactory.create()).thenReturn(givenStorage);

        final ServerUpdateStorage actual = this.configuration.serverUpdateStorage(givenFactory);
        assertSame(givenStorage, actual);
    }
}
