package by.aurorasoft.updatesobserver.storage;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import org.junit.Test;

import java.util.*;

import static by.aurorasoft.updatesobserver.util.ReflectionUtil.findProperty;
import static java.time.Instant.parse;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class ServerUpdateStorageTest {
    private static final String FIELD_NAME_UPDATES_BY_SERVER_NAMES = "updatesByServerNames";

    @Test
    public void storageShouldBeCreated() {
        final ServerUpdate firstGivenUpdate = new ServerUpdate(
                "first-server",
                parse("2023-11-13T10:15:30.00Z"),
                5,
                10
        );
        final ServerUpdate secondGivenUpdate = new ServerUpdate(
                "second-server",
                parse("2023-11-13T11:15:30.00Z"),
                6,
                11
        );
        final ServerUpdate thirdGivenUpdate = new ServerUpdate(
                "third-server",
                parse("2023-11-13T12:15:30.00Z"),
                7,
                12
        );
        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate, thirdGivenUpdate);

        final ServerUpdateStorage actual = new ServerUpdateStorage(givenUpdates);

        final Map<String, ServerUpdate> actualUpdatesByServerNames = findUpdatesByServerNames(actual);
        final Map<String, ServerUpdate> expectedUpdatesByServerNames = Map.of(
                firstGivenUpdate.getServerName(), firstGivenUpdate,
                secondGivenUpdate.getServerName(), secondGivenUpdate,
                thirdGivenUpdate.getServerName(), thirdGivenUpdate
        );
        assertEquals(expectedUpdatesByServerNames, actualUpdatesByServerNames);
    }

    @Test(expected = IllegalStateException.class)
    public void storageShouldNotBeCreatedBecauseOfDuplicatedServerName() {
        final ServerUpdate firstGivenUpdate = new ServerUpdate(
                "first-server",
                parse("2023-11-13T10:15:30.00Z"),
                5,
                10
        );
        final ServerUpdate secondGivenUpdate = new ServerUpdate(
                "second-server",
                parse("2023-11-13T11:15:30.00Z"),
                6,
                11
        );
        final ServerUpdate thirdGivenUpdate = new ServerUpdate(
                "second-server",
                parse("2023-11-13T12:15:30.00Z"),
                7,
                12
        );
        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate, thirdGivenUpdate);

        new ServerUpdateStorage(givenUpdates);
    }

    @Test
    public void updateShouldBeSaved() {
        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(emptyList());
        final ServerUpdate givenUpdate = new ServerUpdate(
                "server",
                parse("2023-11-13T11:15:30.00Z"),
                5,
                10
        );

        givenStorage.save(givenUpdate);

        final Map<String, ServerUpdate> actualUpdatesByServerNames = findUpdatesByServerNames(givenStorage);
        final Map<String, ServerUpdate> expectedUpdatesByServerNames = Map.of(givenUpdate.getServerName(), givenUpdate);
        assertEquals(expectedUpdatesByServerNames, actualUpdatesByServerNames);
    }

    @Test
    public void updateWithExistingServerNameShouldBeSaved() {
        final ServerUpdate firstGivenUpdate = new ServerUpdate(
                "first-server",
                parse("2023-11-13T10:15:30.00Z"),
                5,
                10
        );
        final ServerUpdate secondGivenUpdate = new ServerUpdate(
                "second-server",
                parse("2023-11-13T11:15:30.00Z"),
                6,
                11
        );
        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);
        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenUpdates);

        final ServerUpdate thirdGivenUpdate = new ServerUpdate(
                "second-server",
                parse("2023-11-13T12:15:30.00Z"),
                7,
                12
        );
        givenStorage.save(thirdGivenUpdate);

        final Map<String, ServerUpdate> actualUpdatesByServerNames = findUpdatesByServerNames(givenStorage);
        final Map<String, ServerUpdate> expectedUpdatesByServerNames = Map.of(
                firstGivenUpdate.getServerName(), firstGivenUpdate,
                thirdGivenUpdate.getServerName(), thirdGivenUpdate
        );
        assertEquals(expectedUpdatesByServerNames, actualUpdatesByServerNames);
    }

    @Test
    public void updateShouldBeFoundByServerName() {
        final ServerUpdate firstGivenUpdate = new ServerUpdate(
                "first-server",
                parse("2023-11-13T10:15:30.00Z"),
                5,
                10
        );
        final ServerUpdate secondGivenUpdate = new ServerUpdate(
                "second-server",
                parse("2023-11-13T11:15:30.00Z"),
                6,
                11
        );
        final ServerUpdate thirdGivenUpdate = new ServerUpdate(
                "third-server",
                parse("2023-11-13T12:15:30.00Z"),
                7,
                12
        );
        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate, thirdGivenUpdate);
        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenUpdates);

        final String givenServerName = "third-server";

        final Optional<ServerUpdate> optionalActual = givenStorage.findByServerName(givenServerName);
        assertTrue(optionalActual.isPresent());
        final ServerUpdate actual = optionalActual.get();
        assertEquals(thirdGivenUpdate, actual);
    }

    @Test
    public void updateShouldNotBeFoundByServerName() {
        final ServerUpdate firstGivenUpdate = new ServerUpdate(
                "first-server",
                parse("2023-11-13T10:15:30.00Z"),
                5,
                10
        );
        final ServerUpdate secondGivenUpdate = new ServerUpdate(
                "second-server",
                parse("2023-11-13T11:15:30.00Z"),
                6,
                11
        );
        final ServerUpdate thirdGivenUpdate = new ServerUpdate(
                "third-server",
                parse("2023-11-13T12:15:30.00Z"),
                7,
                12
        );
        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate, thirdGivenUpdate);
        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenUpdates);

        final String givenServerName = "fourth-server";

        final Optional<ServerUpdate> optionalActual = givenStorage.findByServerName(givenServerName);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void allUpdatesShouldBeFound() {
        final ServerUpdate firstGivenUpdate = new ServerUpdate(
                "first-server",
                parse("2023-11-13T10:15:30.00Z"),
                5,
                10
        );
        final ServerUpdate secondGivenUpdate = new ServerUpdate(
                "second-server",
                parse("2023-11-13T11:15:30.00Z"),
                6,
                11
        );
        final ServerUpdate thirdGivenUpdate = new ServerUpdate(
                "third-server",
                parse("2023-11-13T12:15:30.00Z"),
                7,
                12
        );
        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate, thirdGivenUpdate);
        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(givenUpdates);

        final Collection<ServerUpdate> actual = givenStorage.findAll();
        final Set<ServerUpdate> actualAsSet = new HashSet<>(actual);
        final Set<ServerUpdate> expectedAsSet = new HashSet<>(givenUpdates);
        assertEquals(expectedAsSet, actualAsSet);
    }

    @Test
    public void allUpdatesShouldNotBeFound() {
        final ServerUpdateStorage givenStorage = new ServerUpdateStorage(emptyList());

        final Collection<ServerUpdate> actual = givenStorage.findAll();
        assertTrue(actual.isEmpty());
    }

    @SuppressWarnings("unchecked")
    private static Map<String, ServerUpdate> findUpdatesByServerNames(final ServerUpdateStorage storage) {
        return findProperty(
                storage,
                FIELD_NAME_UPDATES_BY_SERVER_NAMES,
                Map.class
        );
    }
}
