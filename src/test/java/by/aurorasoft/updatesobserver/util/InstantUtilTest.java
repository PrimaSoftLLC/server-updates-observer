package by.aurorasoft.updatesobserver.util;

import org.junit.Test;

import java.time.Instant;

import static by.aurorasoft.updatesobserver.util.InstantUtil.plusMinutes;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;

public final class InstantUtilTest {

    @Test
    public void minutesShouldBePlussedToInstant() {
        final Instant givenDateTime = parse("2023-11-16T16:23:30.00Z");
        final int givenMinutes = 2;

        final Instant actual = plusMinutes(givenDateTime, givenMinutes);
        final Instant expected = parse("2023-11-16T16:25:30.00Z");
        assertEquals(expected, actual);
    }

}
