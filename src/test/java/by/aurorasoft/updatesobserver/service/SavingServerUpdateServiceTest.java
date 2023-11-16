package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.base.AbstractContextTest;
import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.service.SavingServerUpdateService.ServerUpdateSavingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextClosedEvent;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.FileUtil.createFile;
import static org.mockito.Mockito.*;

public final class SavingServerUpdateServiceTest extends AbstractContextTest {

    @MockBean
    private ServerUpdateService mockedUpdateService;

    @MockBean
    private ObjectMapper mockedObjectMapper;

    @Value("${server-updates.file.dir}")
    private String expectedUpdateFileDirectoryPath;

    @Value("${server-updates.file.name}")
    private String expectedUpdateFileName;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Test
    public void updatesShouldBeSaved()
            throws Exception {
        final Collection<ServerUpdate> givenUpdates = List.of(
                createUpdate("first-server"),
                createUpdate("second-server")
        );
        when(this.mockedUpdateService.findAll()).thenReturn(givenUpdates);

        final ContextClosedEvent givenEvent = new ContextClosedEvent(this.context);
        this.eventPublisher.publishEvent(givenEvent);

        final File expectedUpdateFile = this.createExpectedUpdateFile();
        verify(this.mockedObjectMapper, times(1)).writeValue(
                eq(expectedUpdateFile),
                same(givenUpdates)
        );
    }

    @Test(expected = ServerUpdateSavingException.class)
    public void updatesShouldNotBeSaved()
            throws Exception {
        final Collection<ServerUpdate> givenUpdates = List.of(
                createUpdate("first-server"),
                createUpdate("second-server")
        );
        when(this.mockedUpdateService.findAll()).thenReturn(givenUpdates);

        final File expectedUpdateFile = this.createExpectedUpdateFile();
        doThrow(IOException.class).when(this.mockedObjectMapper).writeValue(eq(expectedUpdateFile), same(givenUpdates));

        final ContextClosedEvent givenEvent = new ContextClosedEvent(this.context);
        this.eventPublisher.publishEvent(givenEvent);
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }

    private File createExpectedUpdateFile() {
        return createFile(this.expectedUpdateFileDirectoryPath, this.expectedUpdateFileName);
    }
}
