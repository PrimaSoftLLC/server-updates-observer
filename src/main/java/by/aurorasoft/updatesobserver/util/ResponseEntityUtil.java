package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

import static org.springframework.http.CacheControl.maxAge;

@UtilityClass
public final class ResponseEntityUtil {

    public static <T> ResponseEntity<T> noContent() {
        return ResponseEntity.noContent().build();
    }

    public static <T> ResponseEntity<T> ok(final T body, final Duration maxAge) {
        return ResponseEntity.ok()
                .cacheControl(maxAge(maxAge).cachePrivate())
                .body(body);
    }

    //TODO: test
    public static <T> ResponseEntity<T> ok(final T body) {
        return ResponseEntity.ok().body(body);
    }
}
