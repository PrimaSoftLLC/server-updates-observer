package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.MINUTES;

@UtilityClass
public final class InstantUtil {

    public static Instant plusMinutes(final Instant dateTime, final long minutes) {
        return dateTime.plus(minutes, MINUTES);
    }

}
