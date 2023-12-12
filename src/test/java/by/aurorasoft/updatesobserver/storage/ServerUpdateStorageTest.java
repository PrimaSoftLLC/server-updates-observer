package by.aurorasoft.updatesobserver.storage;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import net.jodah.expiringmap.ExpiringMap;
import org.junit.Test;

import java.util.*;

import static by.aurorasoft.updatesobserver.util.ReflectionUtil.findProperty;
import static java.util.Collections.emptyList;
import static java.util.OptionalLong.empty;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static net.jodah.expiringmap.ExpirationPolicy.CREATED;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ServerUpdateStorageTest {
    private static final String FIELD_NAME_UPDATES_BY_SERVER_NAMES = "updatesByServerNames";

    @Test
    public void storageShouldBeCreated() {
        final int givenStorageMaxSize = 20;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 100;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final ServerUpdate secondGivenUpdate = createNotAliveUpdate("second-server");

        final String thirdGivenUpdateServerName = "third-server";
        final long thirdGivenUpdateRemainingLifetimeInMillis = 200;
        final ServerUpdate thirdGivenUpdate = createAliveUpdate(
                thirdGivenUpdateServerName,
                thirdGivenUpdateRemainingLifetimeInMillis
        );

        final Collection<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate, thirdGivenUpdate);

        final ServerUpdateStorage actual = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);

        final ExpiringMap<String, ServerUpdate> actualUpdatesByServerNames = findUpdatesByServerNames(actual);
        final Map<String, ServerUpdate> expectedUpdatesByServerNames = Map.of(
                firstGivenUpdateServerName, firstGivenUpdate,
                thirdGivenUpdateServerName, thirdGivenUpdate
        );
        assertEquals(expectedUpdatesByServerNames, actualUpdatesByServerNames);

        final long actualExpirationFirstGivenUpdate = actualUpdatesByServerNames.getExpiration(
                firstGivenUpdateServerName
        );
        assertEquals(firstGivenUpdateRemainingLifetimeInMillis, actualExpirationFirstGivenUpdate);

        final long actualExpirationThirdGivenUpdate = actualUpdatesByServerNames.getExpiration(
                thirdGivenUpdateServerName
        );
        assertEquals(thirdGivenUpdateRemainingLifetimeInMillis, actualExpirationThirdGivenUpdate);

        final int actualStorageMaxSize = actualUpdatesByServerNames.getMaxSize();
        assertEquals(givenStorageMaxSize, actualStorageMaxSize);

        final boolean allEntriesHaveCreatedExpirationPolicy = allEntriesHaveCreatedExpirationPolicy(
                actualUpdatesByServerNames
        );
        assertTrue(allEntriesHaveCreatedExpirationPolicy);
    }

    @Test
    public void updateShouldBeSaved() {
        final int givenStorageMaxSize = 3;
        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, emptyList());

        final String givenUpdateServerName = "server";
        final long givenUpdateRemainingLifetimeInMillis = 100;
        final ServerUpdate givenUpdate = createAliveUpdate(givenUpdateServerName, givenUpdateRemainingLifetimeInMillis);

        givenStorage.saveIfAlive(givenUpdate);

        final ExpiringMap<String, ServerUpdate> actualUpdatesByServerNames = findUpdatesByServerNames(givenStorage);
        final Map<String, ServerUpdate> expectedUpdatesByServerNames = Map.of(givenUpdate.getServerName(), givenUpdate);
        assertEquals(expectedUpdatesByServerNames, actualUpdatesByServerNames);

        final long actualExpirationGivenUpdate = actualUpdatesByServerNames.getExpiration(givenUpdateServerName);
        assertEquals(givenUpdateRemainingLifetimeInMillis, actualExpirationGivenUpdate);
    }

    @Test
    public void updateWithExistingServerNameShouldBeSaved() {
        final int givenStorageMaxSize = 5;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 100;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final String secondGivenUpdateServerName = "second-server";
        final long secondGivenUpdateRemainingLifetimeInMillis = 200;
        final ServerUpdate secondGivenUpdate = createAliveUpdate(
                secondGivenUpdateServerName,
                secondGivenUpdateRemainingLifetimeInMillis
        );

        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);

        final ServerUpdate thirdGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                300
        );
        givenStorage.saveIfAlive(thirdGivenUpdate);

        final ExpiringMap<String, ServerUpdate> actualUpdatesByServerNames = findUpdatesByServerNames(givenStorage);
        final Map<String, ServerUpdate> expectedUpdatesByServerNames = Map.of(
                firstGivenUpdateServerName, thirdGivenUpdate,
                secondGivenUpdateServerName, secondGivenUpdate
        );
        assertEquals(expectedUpdatesByServerNames, actualUpdatesByServerNames);

        final long actualExpirationThirdGivenUpdate = actualUpdatesByServerNames.getExpiration(
                firstGivenUpdateServerName
        );
        //expiration wasn't changes because of updates are equal
        assertEquals(firstGivenUpdateRemainingLifetimeInMillis, actualExpirationThirdGivenUpdate);
    }

    @Test
    public void updateShouldBeSavedInFullStorage() {
        final int givenStorageMaxSize = 1;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 1000;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);

        final String secondGivenUpdateServerName = "second-server";
        final long secondGivenUpdateRemainingLifetimeInMillis = 200;
        final ServerUpdate secondGivenUpdate = createAliveUpdate(
                secondGivenUpdateServerName,
                secondGivenUpdateRemainingLifetimeInMillis
        );

        givenStorage.saveIfAlive(secondGivenUpdate);

        final ExpiringMap<String, ServerUpdate> actualUpdatesByServerNames = findUpdatesByServerNames(givenStorage);
        final Map<String, ServerUpdate> expectedUpdatesByServerNames = Map.of(
                secondGivenUpdateServerName, secondGivenUpdate
        );
        assertEquals(expectedUpdatesByServerNames, actualUpdatesByServerNames);
    }

    @Test
    public void updateShouldBeFoundByServerName() {
        final int givenStorageMaxSize = 5;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 400;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final String secondGivenUpdateServerName = "second-server";
        final long secondGivenUpdateRemainingLifetimeInMillis = 600;
        final ServerUpdate secondGivenUpdate = createAliveUpdate(
                secondGivenUpdateServerName,
                secondGivenUpdateRemainingLifetimeInMillis
        );

        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);

        final Optional<ServerUpdate> optionalActual = givenStorage.findByServerName(secondGivenUpdateServerName);
        assertTrue(optionalActual.isPresent());
        final ServerUpdate actual = optionalActual.get();
        assertSame(secondGivenUpdate, actual);
    }

    @Test
    public void updateShouldNotBeFoundByServerName() {
        final int givenStorageMaxSize = 5;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 400;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final String secondGivenUpdateServerName = "second-server";
        final long secondGivenUpdateRemainingLifetimeInMillis = 600;
        final ServerUpdate secondGivenUpdate = createAliveUpdate(
                secondGivenUpdateServerName,
                secondGivenUpdateRemainingLifetimeInMillis
        );

        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);

        final String givenServerNameToFindUpdate = "third-server";

        final Optional<ServerUpdate> optionalActual = givenStorage.findByServerName(givenServerNameToFindUpdate);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void updateShouldNotBeFoundByServerNameBecauseOfExpiration()
            throws InterruptedException {
        final int givenStorageMaxSize = 5;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 1000;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final String secondGivenUpdateServerName = "second-server";
        final long secondGivenUpdateRemainingLifetimeInMillis = 400;
        final ServerUpdate secondGivenUpdate = createAliveUpdate(
                secondGivenUpdateServerName,
                secondGivenUpdateRemainingLifetimeInMillis
        );

        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);

        MILLISECONDS.sleep(secondGivenUpdateRemainingLifetimeInMillis * 2);

        final Optional<ServerUpdate> optionalActual = givenStorage.findByServerName(secondGivenUpdateServerName);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void allUpdatesShouldBeFound() {
        final int givenStorageMaxSize = 5;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 1000;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final String secondGivenUpdateServerName = "second-server";
        final long secondGivenUpdateRemainingLifetimeInMillis = 400;
        final ServerUpdate secondGivenUpdate = createAliveUpdate(
                secondGivenUpdateServerName,
                secondGivenUpdateRemainingLifetimeInMillis
        );

        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);

        final Collection<ServerUpdate> actual = givenStorage.findAll();
        final Set<ServerUpdate> actualAsSet = new HashSet<>(actual);
        final Set<ServerUpdate> expectedAsSet = new HashSet<>(givenUpdates);
        assertEquals(expectedAsSet, actualAsSet);
    }

    @Test
    public void allUpdatesShouldNotBeFoundBecauseOfExpiration()
            throws Exception {
        final int givenStorageMaxSize = 5;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 1000;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final String secondGivenUpdateServerName = "second-server";
        final long secondGivenUpdateRemainingLifetimeInMillis = 400;
        final ServerUpdate secondGivenUpdate = createAliveUpdate(
                secondGivenUpdateServerName,
                secondGivenUpdateRemainingLifetimeInMillis
        );

        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);

        MILLISECONDS.sleep(firstGivenUpdateRemainingLifetimeInMillis * 2);

        final Collection<ServerUpdate> actual = givenStorage.findAll();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void updateShouldBeRemovedByServerName() {
        final int givenStorageMaxSize = 5;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 1000;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final String secondGivenUpdateServerName = "second-server";
        final long secondGivenUpdateRemainingLifetimeInMillis = 400;
        final ServerUpdate secondGivenUpdate = createAliveUpdate(
                secondGivenUpdateServerName,
                secondGivenUpdateRemainingLifetimeInMillis
        );

        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);

        final Optional<ServerUpdate> optionalActual = givenStorage.removeByServerName(firstGivenUpdateServerName);
        assertTrue(optionalActual.isPresent());
        final ServerUpdate actual = optionalActual.get();
        assertEquals(firstGivenUpdate, actual);

        final Collection<ServerUpdate> actualStorageUpdates = givenStorage.findAll();
        final Set<ServerUpdate> actualStorageUpdatesAsSet = new HashSet<>(actualStorageUpdates);
        final Set<ServerUpdate> expectedStorageUpdatesAsSet = Set.of(secondGivenUpdate);
        assertEquals(expectedStorageUpdatesAsSet, actualStorageUpdatesAsSet);
    }

    @Test
    public void updateShouldBeNotRemovedByNotExistingServerName() {
        final int givenStorageMaxSize = 5;

        final String firstGivenUpdateServerName = "first-server";
        final long firstGivenUpdateRemainingLifetimeInMillis = 1000;
        final ServerUpdate firstGivenUpdate = createAliveUpdate(
                firstGivenUpdateServerName,
                firstGivenUpdateRemainingLifetimeInMillis
        );

        final String secondGivenUpdateServerName = "second-server";
        final long secondGivenUpdateRemainingLifetimeInMillis = 400;
        final ServerUpdate secondGivenUpdate = createAliveUpdate(
                secondGivenUpdateServerName,
                secondGivenUpdateRemainingLifetimeInMillis
        );

        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);

        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenStorageMaxSize, givenUpdates);
        final String givenServerNameToRemoveUpdate = "not-existing-server";

        final Optional<ServerUpdate> optionalActual = givenStorage.removeByServerName(givenServerNameToRemoveUpdate);
        assertTrue(optionalActual.isEmpty());

        final Collection<ServerUpdate> actualStorageUpdates = givenStorage.findAll();
        final Set<ServerUpdate> actualStorageUpdatesAsSet = new HashSet<>(actualStorageUpdates);
        final Set<ServerUpdate> expectedStorageUpdatesAsSet = new HashSet<>(givenUpdates);
        assertEquals(expectedStorageUpdatesAsSet, actualStorageUpdatesAsSet);
    }

    @SuppressWarnings("unchecked")
    private static ExpiringMap<String, ServerUpdate> findUpdatesByServerNames(final ServerUpdateStorage storage) {
        return findProperty(
                storage,
                FIELD_NAME_UPDATES_BY_SERVER_NAMES,
                ExpiringMap.class
        );
    }

    private static ServerUpdate createAliveUpdate(final String serverName, final long remainingLifetimeInMillis) {
        final ServerUpdate update = createUpdate(serverName);
        when(update.findRemainingLifetimeInMillisIfAlive()).thenReturn(OptionalLong.of(remainingLifetimeInMillis));
        return update;
    }

    @SuppressWarnings("SameParameterValue")
    private static ServerUpdate createNotAliveUpdate(final String serverName) {
        final ServerUpdate update = createUpdate(serverName);
        when(update.findRemainingLifetimeInMillisIfAlive()).thenReturn(empty());
        return update;
    }

    private static ServerUpdate createUpdate(final String serverName) {
        final ServerUpdate update = mock(ServerUpdate.class);
        when(update.getServerName()).thenReturn(serverName);
        return update;
    }

    private static boolean allEntriesHaveCreatedExpirationPolicy(final ExpiringMap<String, ServerUpdate> map) {
        return map.keySet()
                .stream()
                .map(map::getExpirationPolicy)
                .allMatch(policy -> policy == CREATED);
    }
}
