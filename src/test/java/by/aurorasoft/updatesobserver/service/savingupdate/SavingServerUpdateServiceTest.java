package by.aurorasoft.updatesobserver.service.savingupdate;

import by.aurorasoft.updatesobserver.base.AbstractContextTest;
import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.service.ServerUpdateService;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextClosedEvent;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public final class SavingServerUpdateServiceTest extends AbstractContextTest {

    @MockBean
    private ServerUpdateService mockedUpdateService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Test
    public void updatesShouldBeSaved() {
        try (final MockedConstruction<ServerUpdateSerializer> mockedSerializerConstruction = mockConstruction(ServerUpdateSerializer.class)) {
            final Collection<ServerUpdate> givenUpdates = List.of(
                    createUpdate("first-server"),
                    createUpdate("second-server")
            );
            when(this.mockedUpdateService.findAll()).thenReturn(givenUpdates);

            final ContextClosedEvent givenEvent = new ContextClosedEvent(this.context);

            this.eventPublisher.publishEvent(givenEvent);

            final List<ServerUpdateSerializer> constructedSerializers = mockedSerializerConstruction.constructed();
            assertEquals(1, constructedSerializers.size());

            final ServerUpdateSerializer constructedSerializer = constructedSerializers.get(0);
            verify(constructedSerializer, times(1)).serialize(same(givenUpdates));
            verify(constructedSerializer, times(1)).close();
        }
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }
}
