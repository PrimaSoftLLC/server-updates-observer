package by.aurorasoft.updatesobserver.service;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.time.Instant.parse;
import static java.util.Optional.empty;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ServerUpdateServiceTest {

    @Mock
    private ServerUpdateStorage mockedUpdateStorage;

    private ServerUpdateService updateService;

    @Before
    public void initializeUpdateService() {
        this.updateService = new ServerUpdateService(this.mockedUpdateStorage);
    }

    @Test
    public void updateShouldBeSavedIfAlive() {
        final ServerUpdate givenUpdate = ServerUpdate.builder().build();

        this.updateService.saveIfAlive(givenUpdate);

        verify(this.mockedUpdateStorage, times(1)).saveIfAlive(same(givenUpdate));
    }

    @Test
    public void updateDowntimeShouldBeFound() {
        final String givenServerName = "server";

        final Instant givenDowntime = parse("2023-11-15T10:15:30Z");
        final ServerUpdate givenUpdate = createUpdateWithDowntime(givenDowntime);

        when(this.mockedUpdateStorage.findByServerName(same(givenServerName))).thenReturn(Optional.of(givenUpdate));

        final Optional<Instant> optionalActual = this.updateService.findUpdateDowntime(givenServerName);
        assertTrue(optionalActual.isPresent());
        final Instant actual = optionalActual.get();
        assertSame(givenDowntime, actual);
    }

    @Test
    public void updateDowntimeShouldNotBeFound() {
        final String givenServerName = "server";

        when(this.mockedUpdateStorage.findByServerName(same(givenServerName))).thenReturn(empty());

        final Optional<Instant> optionalActual = this.updateService.findUpdateDowntime(givenServerName);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void allUpdatesShouldBeFound() {
        final Collection<ServerUpdate> givenUpdates = List.of(
                createUpdate("first-server"),
                createUpdate("second-server"),
                createUpdate("third-server")
        );

        when(this.mockedUpdateStorage.findAll()).thenReturn(givenUpdates);

        final Collection<ServerUpdate> actual = this.updateService.findAll();
        assertSame(givenUpdates, actual);
    }

    @Test
    public void updateShouldBeRemovedByServerName() {
        final String givenServerName = "server-name";
        final ServerUpdate givenServerUpdate = createUpdate(givenServerName);

        when(this.mockedUpdateStorage.removeByServerName(same(givenServerName)))
                .thenReturn(Optional.of(givenServerUpdate));

        final Optional<ServerUpdate> optionalActual = this.updateService.removeByServerName(givenServerName);
        assertTrue(optionalActual.isPresent());
        final ServerUpdate actual = optionalActual.get();
        assertSame(givenServerUpdate, actual);
    }

    @Test
    public void updateShouldNotBeRemovedByServerName() {
        final String givenServerName = "server-name";

        when(this.mockedUpdateStorage.removeByServerName(same(givenServerName))).thenReturn(empty());

        final Optional<ServerUpdate> optionalActual = this.updateService.removeByServerName(givenServerName);
        assertTrue(optionalActual.isEmpty());
    }

    private static ServerUpdate createUpdateWithDowntime(final Instant downtime) {
        return ServerUpdate.builder()
                .downtime(downtime)
                .build();
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }
}
