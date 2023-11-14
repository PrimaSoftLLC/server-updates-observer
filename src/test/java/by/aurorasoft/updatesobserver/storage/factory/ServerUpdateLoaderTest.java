package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.junit.Test;
import org.mockito.MockedConstruction;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public final class ServerUpdateLoaderTest {
    private static final String GIVEN_FILE_PATH = "file-path";

    private final ServerUpdateLoader loader = new ServerUpdateLoader(GIVEN_FILE_PATH);

    @Test
    public void updatesShouldBeLoaded() {
        final List<ServerUpdate> givenUpdates = List.of(
                createUpdate("first-server"),
                createUpdate("second-server")
        );
        try (final MockedConstruction<ServerUpdateDeserializer> mockedDeserializerConstruction = mockConstruction(
                ServerUpdateDeserializer.class,
                (deserializer, context) -> when(deserializer.deserialize()).thenReturn(givenUpdates)
        )) {
            final List<ServerUpdate> actual = this.loader.load();
            assertSame(givenUpdates, actual);

            final List<ServerUpdateDeserializer> createdDeserializers = mockedDeserializerConstruction.constructed();
            assertEquals(1, createdDeserializers.size());

            final ServerUpdateDeserializer createdDeserializer = createdDeserializers.get(0);
            verify(createdDeserializer, times(1)).close();
        }
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }
}
