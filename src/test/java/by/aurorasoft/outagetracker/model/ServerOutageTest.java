package by.aurorasoft.outagetracker.model;

import org.junit.Test;

import java.time.Instant;
import java.util.OptionalLong;
import java.util.stream.Stream;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.Assert.assertTrue;

public final class ServerOutageTest {

    @Test
    public void remainingLifetimeInMillisShouldBeFound() {
        final Stream<ServerOutage> givenUpdates = Stream.of(
                createUpdate(now().plus(100, MILLIS)),
                createUpdate(now().plus(100, SECONDS)),
                createUpdate(now().plus(100, MINUTES)),
                createUpdate(now().plus(100, HOURS)),
                createUpdate(now().plus(100, DAYS))
        );

        final boolean actual = givenUpdates
                .map(ServerOutage::findRemainingLifetimeInMillisIfAlive)
                .allMatch(OptionalLong::isPresent);
        assertTrue(actual);
    }

    @Test
    public void updatesShouldNotBeAlive() {
        final Stream<ServerOutage> givenUpdates = Stream.of(
                createUpdate(now().minus(100, MILLIS)),
                createUpdate(now().minus(100, SECONDS)),
                createUpdate(now().minus(100, MINUTES)),
                createUpdate(now().minus(100, HOURS)),
                createUpdate(now().minus(100, DAYS))
        );

        final boolean actual = givenUpdates
                .map(ServerOutage::findRemainingLifetimeInMillisIfAlive)
                .allMatch(OptionalLong::isEmpty);
        assertTrue(actual);
    }

    private static ServerOutage createUpdate(final Instant dateTime) {
        return ServerOutage.builder()
                .lifetime(dateTime)
                .build();
    }
}
