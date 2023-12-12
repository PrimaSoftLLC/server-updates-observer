package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.base.AbstractContextTest;
import by.aurorasoft.updatesobserver.configuration.ServerUpdateFilePath;
import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.service.RefreshingServerUpdateService.ServerUpdateRefreshingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.FileUtil.createFile;
import static org.mockito.Mockito.*;

public final class RefreshingServerUpdateServiceTest extends AbstractContextTest {

    private static final Collection<ServerUpdate> GIVEN_UPDATES = List.of(
            createUpdate("first-server"),
            createUpdate("second-server")
    );

    @MockBean
    private ServerUpdateService mockedUpdateService;

    @MockBean
    private ObjectMapper mockedObjectMapper;

    @Autowired
    private ServerUpdateFilePath serverUpdateFilePath;

    @Autowired
    private RefreshingServerUpdateService refreshingService;

    @Test
    public void updatesShouldBeRefreshed() throws Exception {
        mockUpdateService();

        refreshingService.refresh();

        File expectedUpdateFile = createExpectedUpdateFile();
        verify(mockedObjectMapper, times(1)).writeValue(
                eq(expectedUpdateFile),
                same(GIVEN_UPDATES)
        );
    }

    @Test(expected = ServerUpdateRefreshingException.class)
    public void updatesShouldNotBeRefreshed() throws Exception {
        mockUpdateService();

        doThrow(IOException.class).when(mockedObjectMapper).writeValue(any(File.class), anyList());

        refreshingService.refresh();
    }

    private void mockUpdateService() {
        when(this.mockedUpdateService.getAll()).thenReturn(GIVEN_UPDATES);
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }

    private File createExpectedUpdateFile() {
        return createFile(this.serverUpdateFilePath);
    }
}
