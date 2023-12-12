package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.configuration.ServerUpdateFilePath;
import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.storage.factory.ServerUpdateLoader.ServerUpdateLoadingException;
import by.aurorasoft.updatesobserver.util.FileUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.FileUtil.createFile;
import static by.aurorasoft.updatesobserver.util.FileUtil.isEmpty;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ServerUpdateLoaderTest {
    private static final String GIVEN_UPDATE_FILE_DIRECTORY_PATH = "directory";
    private static final String GIVEN_UPDATE_FILE_NAME = "file";
    private static final ServerUpdateFilePath GIVEN_SERVER_UPDATE_FILE_PATH = new ServerUpdateFilePath(
            GIVEN_UPDATE_FILE_DIRECTORY_PATH,
            GIVEN_UPDATE_FILE_NAME
    );
    private static final File EXPECTED_UPDATE_FILE = createFile(GIVEN_SERVER_UPDATE_FILE_PATH);

    @Mock
    private ObjectMapper mockedObjectMapper;

    private ServerUpdateLoader loader;

    @Before
    public void initializeLoader() {
        this.loader = new ServerUpdateLoader(this.mockedObjectMapper, GIVEN_SERVER_UPDATE_FILE_PATH);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updatesShouldBeLoaded()
            throws Exception {
        try (final MockedStatic<FileUtil> mockedFileUtil = mockStatic(FileUtil.class)) {
            mockedFileUtil.when(() -> isEmpty(eq(EXPECTED_UPDATE_FILE))).thenReturn(false);

            final List<ServerUpdate> givenUpdates = List.of(
                    createUpdate("first-server"),
                    createUpdate("second-server")
            );
            when(this.mockedObjectMapper.readValue(eq(EXPECTED_UPDATE_FILE), any(TypeReference.class)))
                    .thenReturn(givenUpdates);

            final Collection<ServerUpdate> actual = this.loader.load();
            assertSame(givenUpdates, actual);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updatesShouldBeLoadedFromEmptyFile()
            throws Exception {
        try (final MockedStatic<FileUtil> mockedFileUtil = mockStatic(FileUtil.class)) {
            mockedFileUtil.when(() -> isEmpty(eq(EXPECTED_UPDATE_FILE))).thenReturn(true);

            final Collection<ServerUpdate> actual = this.loader.load();
            assertTrue(actual.isEmpty());

            verify(this.mockedObjectMapper, times(0)).readValue(
                    any(File.class),
                    any(TypeReference.class)
            );
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = ServerUpdateLoadingException.class)
    public void updatesShouldNotBeLoaded()
            throws Exception {
        try (final MockedStatic<FileUtil> mockedFileUtil = mockStatic(FileUtil.class)) {
            mockedFileUtil.when(() -> isEmpty(eq(EXPECTED_UPDATE_FILE))).thenReturn(false);

            when(this.mockedObjectMapper.readValue(eq(EXPECTED_UPDATE_FILE), any(TypeReference.class)))
                    .thenThrow(IOException.class);

            this.loader.load();
        }
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }
}
