package by.aurorasoft.outagetracker.service;

import by.aurorasoft.outagetracker.model.ServerOutage;
import by.aurorasoft.outagetracker.storage.ServerOutageStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ServerOutageServiceTest {

    private static final String GIVEN_SERVER_NAME = "server-name";

    @Mock
    private ServerOutageStorage mockedUpdateStorage;
    @Mock
    private ServerOutageBackupService mockedBackupService;

    private ServerOutageService updateService;

    @Before
    public void initializeUpdateService() {
        updateService = new ServerOutageService(mockedUpdateStorage, mockedBackupService);
    }

    @Test
    public void save_AliveOutage_ShouldInvokeStorageSave() {
        ServerOutage givenServerOutage = ServerOutage.builder().build();

        updateService.save(givenServerOutage);

        verify(mockedUpdateStorage, times(1)).save(same(givenServerOutage));
        verify(mockedBackupService, times(1)).backup();
    }

    @Test
    public void getDowntime_ExistingOutage_ShouldReturnDowntime() {
        Instant givenDowntime = Instant.parse("2023-11-15T10:15:30Z");
        ServerOutage givenServerOutage = ServerOutage.builder().downtime(givenDowntime).build();

        when(mockedUpdateStorage.findByServerName(GIVEN_SERVER_NAME))
                .thenReturn(Optional.of(givenServerOutage));

        Optional<Instant> actual = updateService.getDowntime(GIVEN_SERVER_NAME);

        assertSame(givenDowntime, actual.orElseThrow());
    }

    @Test
    public void getDowntime_NonExistingOutage_ShouldReturnEmpty() {
        when(mockedUpdateStorage.findByServerName(GIVEN_SERVER_NAME)).thenReturn(Optional.empty());

        Optional<Instant> actual = updateService.getDowntime(GIVEN_SERVER_NAME);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void getAll_WhenCalled_ShouldReturnAllOutages() {
        Collection<ServerOutage> givenServerOutages = List.of(
                createUpdate("first-server"),
                createUpdate("second-server"),
                createUpdate("third-server")
        );

        when(mockedUpdateStorage.findAll()).thenReturn(givenServerOutages);

        Collection<ServerOutage> actual = updateService.getAll();

        assertSame(givenServerOutages, actual);
    }

    @Test
    public void remove_ExistingServerName_ShouldRemoveOutage() {
        ServerOutage givenServerOutage = createUpdate(GIVEN_SERVER_NAME);

        when(mockedUpdateStorage.removeByServerName(GIVEN_SERVER_NAME))
                .thenReturn(Optional.of(givenServerOutage));

        updateService.remove(GIVEN_SERVER_NAME);
        Optional<Instant> actual = updateService.getDowntime(GIVEN_SERVER_NAME);

        assertTrue(actual.isEmpty());

        verify(mockedBackupService, times(1)).backup();
    }

    @Test
    public void remove_NonExistingServerName_ShoulNotThrows() {
        when(mockedUpdateStorage.removeByServerName(GIVEN_SERVER_NAME)).thenReturn(Optional.empty());

        updateService.remove(GIVEN_SERVER_NAME);

        verify(mockedBackupService, times(1)).backup();
    }

    private static ServerOutage createUpdate(String serverName) {
        return ServerOutage.builder().serverName(serverName).build();
    }
}
