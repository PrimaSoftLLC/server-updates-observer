package by.aurorasoft.updatesobserver.model;

import org.junit.Test;
import org.mockito.MockedStatic;

import java.time.*;
import java.util.stream.Stream;

import static java.time.Instant.now;
import static java.time.Instant.parse;
import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockStatic;

public final class ServerUpdateTest {
//
//    @Test
//    public void updatesShouldBeAlive() {
//        Clock clock = Clock.fixed(parse("2026-11-15T14:17:29Z"), ZoneId.of("UTC"));
//        Instant givenNow = now(clock);
//
//        final Stream<ServerUpdate> givenUpdates = Stream.of(
//                createUpdate(LocalDateTime.of(2023, 11, 15, 14, 17, 30)),
//                createUpdate(LocalDateTime.of(2023, 11, 15, 14, 17, 31)),
//                createUpdate(LocalDateTime.of(2023, 11, 15, 14, 18, 30)),
//                createUpdate(LocalDateTime.of(2023, 11, 15, 15, 17, 30)),
//                createUpdate(LocalDateTime.of(2023, 11, 16, 14, 17, 30)),
//                createUpdate(LocalDateTime.of(2023, 12, 15, 14, 17, 30)),
//                createUpdate(LocalDateTime.of(2024, 11, 15, 14, 17, 30))
//        );
//
//        final boolean actual = givenUpdates.allMatch(ServerUpdate::isAlive);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void updatesShouldNotBeAlive() {
//        final Stream<ServerUpdate> givenUpdates = Stream.of(
//                createUpdate(parse("2023-11-15T14:17:29Z")),
//                createUpdate(parse("2023-11-15T14:17:28Z")),
//                createUpdate(parse("2023-11-15T14:16:30Z")),
//                createUpdate(parse("2023-11-15T13:17:30Z")),
//                createUpdate(parse("2023-11-14T14:17:30Z")),
//                createUpdate(parse("2023-10-15T14:17:30Z"))
//        );
//
//        final boolean actual = givenUpdates.noneMatch(ServerUpdate::isAlive);
//        assertTrue(actual);
//    }
//
//    private static ServerUpdate createUpdate(final LocalDateTime dateTime) {
//        return ServerUpdate.builder()
//                .lifetime(dateTime.toInstant(UTC))
//                .build();
//    }
}
