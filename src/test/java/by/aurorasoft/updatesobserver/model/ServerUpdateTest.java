package by.aurorasoft.updatesobserver.model;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.stream.Stream;

import static java.time.Instant.parse;
import static org.junit.Assert.assertTrue;

public final class ServerUpdateTest {

    @Test
    public void updatesShouldBeAlive() {
        try (final MockedStatic<Instant> mockedStaticInstant = Mockito.mockStatic(Instant.class)) {
            final Instant givenNow = parse("2023-11-15T14:17:30Z");
            mockedStaticInstant.when(Instant::now).thenReturn(givenNow);

            final Stream<ServerUpdate> givenUpdates = Stream.of(
                    createUpdate(parse("2023-11-15T14:17:30Z")),
                    createUpdate(parse("2023-11-15T14:17:31Z")),
                    createUpdate(parse("2023-11-15T14:18:31Z")),
                    createUpdate(parse("2023-11-15T15:18:31Z")),
                    createUpdate(parse("2023-11-15T16:18:31Z")),
                    createUpdate(parse("2023-11-16T16:18:31Z")),
                    createUpdate(parse("2023-12-16T16:18:31Z")),
                    createUpdate(parse("2024-12-16T16:18:31Z"))
            );

            final boolean actual = givenUpdates.allMatch(ServerUpdate::isAlive);
            assertTrue(actual);
        }
    }

    @Test
    public void updatesShouldNotBeAlive() {
        final Stream<ServerUpdate> givenUpdates = Stream.of(
                createUpdate(parse("2023-11-15T14:17:29Z")),
                createUpdate(parse("2023-11-15T14:17:28Z")),
                createUpdate(parse("2023-11-15T14:16:30Z")),
                createUpdate(parse("2023-11-15T13:17:30Z")),
                createUpdate(parse("2023-11-14T14:17:30Z")),
                createUpdate(parse("2023-10-15T14:17:30Z"))
        );

        final boolean actual = givenUpdates.noneMatch(ServerUpdate::isAlive);
        assertTrue(actual);
    }

    private static ServerUpdate createUpdate(final Instant lifetime) {
        return ServerUpdate.builder()
                .lifetime(lifetime)
                .build();
    }
}
