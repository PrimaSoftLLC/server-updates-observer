package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.base.AbstractContextTest;
import by.aurorasoft.updatesobserver.configuration.property.ServerUpdateFilePath;
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

    @MockBean
    private ServerUpdateService mockedUpdateService;

    @MockBean
    private ObjectMapper mockedObjectMapper;

    @Autowired
    private ServerUpdateFilePath serverUpdateFilePath;

    @Autowired
    private RefreshingServerUpdateService refreshingService;

    @Test
    public void updatesShouldBeRefreshed()
            throws Exception {
        final Collection<ServerUpdate> givenUpdates = List.of(
                createUpdate("first-server"),
                createUpdate("second-server")
        );
        when(this.mockedUpdateService.findAll()).thenReturn(givenUpdates);

        this.refreshingService.refresh();

        final File expectedUpdateFile = this.createExpectedUpdateFile();
        verify(this.mockedObjectMapper, times(1)).writeValue(
                eq(expectedUpdateFile),
                same(givenUpdates)
        );
    }

    @Test(expected = ServerUpdateRefreshingException.class)
    public void updatesShouldNotBeRefreshed()
            throws Exception {
        final Collection<ServerUpdate> givenUpdates = List.of(
                createUpdate("first-server"),
                createUpdate("second-server")
        );
        when(this.mockedUpdateService.findAll()).thenReturn(givenUpdates);

        final File expectedUpdateFile = this.createExpectedUpdateFile();
        doThrow(IOException.class).when(this.mockedObjectMapper).writeValue(eq(expectedUpdateFile), same(givenUpdates));

        this.refreshingService.refresh();
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
