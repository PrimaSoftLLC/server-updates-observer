package by.aurorasoft.outagetracker.storage;

import by.aurorasoft.outagetracker.model.ServerOutage;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ServerOutageStorageTest {

    private ServerOutageStorage storage;
    private static final int STORAGE_MAX_SIZE = 5;

    @Before
    public void setUp() {
        storage = new ServerOutageStorage(STORAGE_MAX_SIZE, List.of());
    }

    @Test
    public void save_aliveAndDeadOutages_onlyAliveStored() {
        ServerOutage givenAliveServerOutage = createAliveServerOutage("server-alive");
        ServerOutage givenExpiredServerOutage = createExpiredServerOutage();

        storage.save(givenAliveServerOutage);
        storage.save(givenExpiredServerOutage);

        assertTrue(storage.findByServerName("server-alive").isPresent());
        assertFalse(storage.findByServerName("server-dead").isPresent());
    }

    @Test
    public void save_overflowMaxSize_evictOldestOutage() {
        for (int i = 0; i < STORAGE_MAX_SIZE + 1; i++) {
            storage.save(createAliveServerOutage("server-" + i));
        }

        assertFalse(storage.findByServerName("server-0").isPresent());
        assertTrue(storage.findByServerName("server-" + (STORAGE_MAX_SIZE)).isPresent());
    }

    @Test
    public void findByServerName_existingOutage_returnsOutage() {
        ServerOutage serverOutage = createAliveServerOutage("server-find");
        storage.save(serverOutage);

        Optional<ServerOutage> actual = storage.findByServerName("server-find");
        assertTrue(actual.isPresent());
        assertSame(serverOutage, actual.get());
    }

    @Test
    public void findByServerName_nonexistentOutage_returnsEmpty() {
        Optional<ServerOutage> actual = storage.findByServerName("nonexistent-server");
        assertFalse(actual.isPresent());
    }

    @Test
    public void removeByServerName_existingOutage_removesAndReturnsOutage() {
        ServerOutage givenServerOutage = createAliveServerOutage("server-remove");
        storage.save(givenServerOutage);

        Optional<ServerOutage> actual = storage.removeByServerName("server-remove");
        assertTrue(actual.isPresent());
        assertSame(givenServerOutage, actual.get());

        assertFalse(storage.findByServerName("server-remove").isPresent());
    }

    @Test
    public void removeByServerName_NonexistentOutage_ReturnsEmpty() {
        Optional<ServerOutage> actual = storage.removeByServerName("nonexistent-server");
        assertFalse(actual.isPresent());
    }

    private ServerOutage createAliveServerOutage(String serverName) {
        ServerOutage serverOutage = mock(ServerOutage.class);
        when(serverOutage.getServerName()).thenReturn(serverName);
        when(serverOutage.findRemainingLifetimeInMillisIfAlive()).thenReturn(OptionalLong.of(1000));
        return serverOutage;
    }

    private ServerOutage createExpiredServerOutage() {
        ServerOutage serverOutage = mock(ServerOutage.class);
        when(serverOutage.getServerName()).thenReturn("server-expired");
        when(serverOutage.findRemainingLifetimeInMillisIfAlive()).thenReturn(OptionalLong.empty());
        return serverOutage;
    }
}
