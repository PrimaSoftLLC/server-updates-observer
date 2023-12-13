package by.aurorasoft.outagetracker.storage.factory;

import by.aurorasoft.outagetracker.configuration.ServerOutageFilePath;
import by.aurorasoft.outagetracker.model.ServerOutage;
import by.aurorasoft.outagetracker.storage.factory.ServerOutageBackupRestorer.ServerOutageLoadingException;
import by.aurorasoft.outagetracker.util.FileUtil;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ServerOutageBackupRestorerTest {
    private static final ServerOutageFilePath GIVEN_SERVER_UPDATE_FILE_PATH =
            new ServerOutageFilePath("directory", "file");
    private static final File EXPECTED_UPDATE_FILE = createFile();

    @Mock
    private ObjectMapper mockedObjectMapper;

    private ServerOutageBackupRestorer restorer;

    @Before
    public void initializeLoader() {
        restorer = new ServerOutageBackupRestorer(mockedObjectMapper, GIVEN_SERVER_UPDATE_FILE_PATH);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void restore_NonEmptyFile_ReturnsUpdates() throws Exception {
        try (MockedStatic<FileUtil> mockedFileUtil = mockStatic(FileUtil.class)) {
            mockedFileUtil.when(() -> FileUtil.isEmpty(eq(EXPECTED_UPDATE_FILE))).thenReturn(false);

            List<ServerOutage> givenServerOutages = List.of(
                    createServerOutage("first-server"),
                    createServerOutage("second-server"));

            when(mockedObjectMapper.readValue(eq(EXPECTED_UPDATE_FILE), any(TypeReference.class)))
                    .thenReturn(givenServerOutages);

            Collection<ServerOutage> actual = restorer.restore();
            assertSame(givenServerOutages, actual);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void restore_EmptyFile_ReturnsEmptyCollection() throws Exception {
        try (MockedStatic<FileUtil> mockedFileUtil = mockStatic(FileUtil.class)) {
            mockedFileUtil.when(() -> FileUtil.isEmpty(eq(EXPECTED_UPDATE_FILE))).thenReturn(true);

            Collection<ServerOutage> actual = restorer.restore();
            assertTrue(actual.isEmpty());
            verify(mockedObjectMapper, never()).readValue(any(File.class), any(TypeReference.class));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void restore_IOException_ThrowsServerOutageLoadingException() throws IOException {
        try (MockedStatic<FileUtil> mockedFileUtil = mockStatic(FileUtil.class)) {
            mockedFileUtil.when(() -> FileUtil.isEmpty(eq(EXPECTED_UPDATE_FILE))).thenReturn(false);
            when(mockedObjectMapper.readValue(eq(EXPECTED_UPDATE_FILE), any(TypeReference.class)))
                    .thenThrow(IOException.class);

            assertThrows(ServerOutageLoadingException.class, () -> restorer.restore());
        }
    }

    private static ServerOutage createServerOutage(String serverName) {
        return ServerOutage.builder().serverName(serverName).build();
    }

    private static File createFile() {
        // Simulate file creation logic
        return new File(GIVEN_SERVER_UPDATE_FILE_PATH.getDirectoryPath(), GIVEN_SERVER_UPDATE_FILE_PATH.getFileName());
    }
}
