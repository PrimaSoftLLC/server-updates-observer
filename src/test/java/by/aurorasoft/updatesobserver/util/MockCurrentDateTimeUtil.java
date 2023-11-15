package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;
import org.mockito.MockedStatic;

import java.time.Clock;
import java.time.Instant;

import static org.mockito.Mockito.*;

@UtilityClass
public final class MockCurrentDateTimeUtil {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static MockedStatic<Clock> mockCurrentDateTime(final Instant dateTime) {
        final Clock spyClock = spy(Clock.class);
        final MockedStatic<Clock> mockedStaticClock = mockStatic(Clock.class);
        mockedStaticClock.when(Clock::systemUTC).thenReturn(spyClock);
        when(spyClock.instant()).thenReturn(dateTime);
        return mockedStaticClock;
    }

}
