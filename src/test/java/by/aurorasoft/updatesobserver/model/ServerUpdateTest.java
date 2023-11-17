//package by.aurorasoft.updatesobserver.model;
//
//import org.junit.Test;
//
//import java.time.Instant;
//import java.util.stream.Stream;
//
//import static java.time.Instant.now;
//import static java.time.temporal.ChronoUnit.*;
//import static org.junit.Assert.assertTrue;
//
//public final class ServerUpdateTest {
//
//    @Test
//    public void updatesShouldBeAlive() {
//        final Stream<ServerUpdate> givenUpdates = Stream.of(
//                createUpdate(now().plus(100, MILLIS)),
//                createUpdate(now().plus(100, SECONDS)),
//                createUpdate(now().plus(100, MINUTES)),
//                createUpdate(now().plus(100, HOURS)),
//                createUpdate(now().plus(100, DAYS))
//        );
//        final boolean actual = givenUpdates.allMatch(ServerUpdate::isAlive);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void updatesShouldNotBeAlive() {
//        final Stream<ServerUpdate> givenUpdates = Stream.of(
//                createUpdate(now().minus(100, MILLIS)),
//                createUpdate(now().minus(100, SECONDS)),
//                createUpdate(now().minus(100, MINUTES)),
//                createUpdate(now().minus(100, HOURS)),
//                createUpdate(now().minus(100, DAYS))
//        );
//        final boolean actual = givenUpdates.noneMatch(ServerUpdate::isAlive);
//        assertTrue(actual);
//    }
//
//    private static ServerUpdate createUpdate(final Instant dateTime) {
//        return ServerUpdate.builder()
//                .lifetime(dateTime)
//                .build();
//    }
//}
