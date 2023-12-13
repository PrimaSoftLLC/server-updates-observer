package by.aurorasoft.outagetracker.service;

import by.aurorasoft.outagetracker.base.AbstractContextTest;
import by.aurorasoft.outagetracker.configuration.ServerOutageFilePath;
import by.aurorasoft.outagetracker.model.ServerOutage;
import by.aurorasoft.outagetracker.service.BackupServerOutageService.ServerOutageBackupException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static by.aurorasoft.outagetracker.util.FileUtil.createFile;
import static org.mockito.Mockito.*;

public final class BackupServerOutageServiceTest extends AbstractContextTest {

    private static final Collection<ServerOutage> GIVEN_SERVER_OUTAGES = List.of(
            createServerOutage("first-server"),
            createServerOutage("second-server")
    );

    @MockBean
    private ServerOutageService mockedUpdateService;

    @MockBean
    private ObjectMapper mockedObjectMapper;

    @Autowired
    private ServerOutageFilePath serverOutageFilePath;

    @Autowired
    private BackupServerOutageService backupService;

    @Test
    public void updatesShouldBeRefreshed() throws Exception {
        mockBackupService();

        backupService.backup();

        File expectedUpdateFile = createExpectedUpdateFile();
        verify(mockedObjectMapper, times(1)).writeValue(
                eq(expectedUpdateFile),
                same(GIVEN_SERVER_OUTAGES)
        );
    }

    @Test(expected = ServerOutageBackupException.class)
    public void updatesShouldNotBeRefreshed() throws Exception {
        mockBackupService();

        doThrow(IOException.class).when(mockedObjectMapper).writeValue(any(File.class), anyList());

        backupService.backup();
    }

    private void mockBackupService() {
        when(this.mockedUpdateService.getAll()).thenReturn(GIVEN_SERVER_OUTAGES);
    }

    private static ServerOutage createServerOutage(final String serverName) {
        return ServerOutage.builder()
                .serverName(serverName)
                .build();
    }

    private File createExpectedUpdateFile() {
        return createFile(this.serverOutageFilePath);
    }
}
