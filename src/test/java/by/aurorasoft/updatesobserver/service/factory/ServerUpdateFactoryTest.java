package by.aurorasoft.updatesobserver.service.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.util.InstantUtil;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.time.Instant;

import static by.aurorasoft.updatesobserver.util.InstantUtil.plusMinutes;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

public final class ServerUpdateFactoryTest {
    private final ServerUpdateFactory factory = new ServerUpdateFactory();

    @Test
    public void updateShouldBeCreated() {
        try (final MockedStatic<Instant> mockedStaticInstant = mockStatic(Instant.class);
             final MockedStatic<InstantUtil> mockedStaticInstantUtil = mockStatic(InstantUtil.class)) {
            final String givenServerName = "server";
            final int givenDowntimeInMinutes = 5;
            final int givenExtraLifetimeInMinutes = 10;

            final Instant givenNow = parse("2023-11-15T10:15:30.00Z");
            mockedStaticInstant.when(Instant::now).thenReturn(givenNow);

            final Instant givenDowntime = parse("2023-11-15T10:20:30.00Z");
            mockedStaticInstantUtil.when(() -> plusMinutes(same(givenNow), eq(givenDowntimeInMinutes)))
                    .thenReturn(givenDowntime);

            final Instant givenLifetime = parse("2023-11-15T10:30:30.00Z");
            mockedStaticInstantUtil.when(() -> plusMinutes(same(givenNow), eq(givenExtraLifetimeInMinutes)))
                    .thenReturn(givenDowntime);

            final ServerUpdate actual = this.factory.create(
                    givenServerName,
                    givenDowntimeInMinutes,
                    givenExtraLifetimeInMinutes
            );
            final ServerUpdate expected = new ServerUpdate(givenServerName, givenNow, givenDowntime, givenLifetime);
            assertEquals(expected, actual);
        }
    }
}
