package by.aurorasoft.updatesobserver.util;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

import static by.aurorasoft.updatesobserver.util.ResponseEntityUtil.noContent;
import static by.aurorasoft.updatesobserver.util.ResponseEntityUtil.ok;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.CacheControl.maxAge;

public final class ResponseEntityUtilTest {

    @Test
    public void noContentResponseEntityShouldBeCreated() {
        final ResponseEntity<?> actual = noContent();
        final ResponseEntity<?> expected = ResponseEntity.noContent().build();
        assertEquals(expected, actual);
    }

    @Test
    public void okResponseEntityWithMaxAgeShouldBeCreated() {
        final Object givenBody = new Object();
        final Duration givenMaxAge = Duration.of(1, SECONDS);

        final ResponseEntity<Object> actual = ok(givenBody, givenMaxAge);
        final ResponseEntity<Object> expected = ResponseEntity.ok()
                .cacheControl(maxAge(givenMaxAge).cachePrivate())
                .body(givenBody);
        assertEquals(expected, actual);
    }

}
