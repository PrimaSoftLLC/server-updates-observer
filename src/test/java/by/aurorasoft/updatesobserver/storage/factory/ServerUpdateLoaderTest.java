package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.storage.factory.ServerUpdateLoader.ServerUpdateLoadingException;
import by.aurorasoft.updatesobserver.util.InputStreamUtil;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.InputStreamUtil.createObjectInputStream;
import static by.aurorasoft.updatesobserver.util.InputStreamUtil.readObjects;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public final class ServerUpdateLoaderTest {
    private static final String GIVEN_FILE_PATH = "file-path";

    private final ServerUpdateLoader loader = new ServerUpdateLoader(GIVEN_FILE_PATH);

    @Test
    public void updatesShouldBeLoaded()
            throws IOException {
        try (final MockedStatic<InputStreamUtil> mockedStreamUtil = mockStatic(InputStreamUtil.class)) {
            final ObjectInputStream givenInputStream = mock(ObjectInputStream.class);
            mockedStreamUtil.when(() -> createObjectInputStream(same(GIVEN_FILE_PATH))).thenReturn(givenInputStream);

            final List<ServerUpdate> givenUpdates = List.of(
                    createUpdate("first-server"),
                    createUpdate("second-server")
            );
            mockedStreamUtil.when(() -> readObjects(same(givenInputStream), same(ServerUpdate.class)))
                    .thenReturn(givenUpdates);

            final List<ServerUpdate> actual = this.loader.load();
            assertSame(givenUpdates, actual);

            verify(givenInputStream, times(1)).close();
        }
    }

    @Test(expected = ServerUpdateLoadingException.class)
    public void updatesShouldNotBeLoadedBecauseOfExceptionWhileClosingStream()
            throws Exception {
        try (final MockedStatic<InputStreamUtil> mockedStreamUtil = mockStatic(InputStreamUtil.class)) {
            final ObjectInputStream givenInputStream = mock(ObjectInputStream.class);
            mockedStreamUtil.when(() -> createObjectInputStream(same(GIVEN_FILE_PATH))).thenReturn(givenInputStream);

            final List<ServerUpdate> givenUpdates = List.of(
                    createUpdate("first-server"),
                    createUpdate("second-server")
            );
            mockedStreamUtil.when(() -> readObjects(same(givenInputStream), same(ServerUpdate.class)))
                    .thenReturn(givenUpdates);

            doThrow(IOException.class).when(givenInputStream).close();

            this.loader.load();
        }
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }
}
