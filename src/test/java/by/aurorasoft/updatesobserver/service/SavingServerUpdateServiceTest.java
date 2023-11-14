package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.base.AbstractContextTest;
import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.util.SerializationUtil;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextClosedEvent;

import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.SerializationUtil.createObjectOutputStream;
import static by.aurorasoft.updatesobserver.util.SerializationUtil.writeObjects;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public final class SavingServerUpdateServiceTest extends AbstractContextTest {

    @MockBean
    private ServerUpdateService mockedUpdateService;

    @Value("${server-updates.file-path}")
    private String expectedUpdateFilePath;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Test
    public void updatesShouldBeSaved()
            throws Exception {
        try (final MockedStatic<SerializationUtil> mockedStreamUtil = mockStatic(SerializationUtil.class)) {
            final ObjectOutputStream givenOutputStream = mock(ObjectOutputStream.class);
            mockedStreamUtil.when(() -> createObjectOutputStream(eq(this.expectedUpdateFilePath)))
                    .thenReturn(givenOutputStream);

            final Collection<ServerUpdate> givenUpdates = List.of(
                    createUpdate("first-server"),
                    createUpdate("second-server")
            );
            when(this.mockedUpdateService.findAll()).thenReturn(givenUpdates);

            final ContextClosedEvent givenEvent = new ContextClosedEvent(this.context);
            this.eventPublisher.publishEvent(givenEvent);

            mockedStreamUtil.verify(
                    () -> writeObjects(same(givenOutputStream), same(givenUpdates)),
                    times(1)
            );

            verify(givenOutputStream, times(1)).close();
        }
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }
}
