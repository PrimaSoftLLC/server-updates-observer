package by.aurorasoft.outagetracker.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

import static org.springframework.http.CacheControl.maxAge;

@UtilityClass
public class ResponseEntityUtil {

    public <T> ResponseEntity<T> noContent() {
        return ResponseEntity.noContent().build();
    }

    public <T> ResponseEntity<T> ok(final T body, final Duration maxAge) {
        return ResponseEntity.ok()
                .cacheControl(maxAge(maxAge).cachePrivate())
                .body(body);
    }
}
