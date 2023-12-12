package by.aurorasoft.updatesobserver.controller;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.service.ServerUpdateService;
import by.aurorasoft.updatesobserver.service.factory.ServerUpdateFactory;
import by.aurorasoft.updatesobserver.util.ResponseEntityUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequestMapping("/serverUpdate")
@RequiredArgsConstructor
@Validated
public class ServerUpdateController {
    private static final Duration UPDATE_DOWNTIME_CACHE_DURATION = Duration.of(1, DAYS);

    private final ServerUpdateFactory factory;
    private final ServerUpdateService service;

    /**
     * @param serverName             the name of the server
     * @param downtimeInMinutes      the downtime in minutes
     * @param extraLifetimeInMinutes the extra lifetime in minutes after downtime, on this time will be return downtime
     * @return a response entity indicating the outcome of the operation
     */
    @PostMapping
    public ResponseEntity<Void> create(@RequestParam(name = "serverName") @NotBlank String serverName,
                                       @RequestParam(name = "downtime") @Min(1) long downtimeInMinutes,
                                       @RequestParam(name = "extraLifetime", defaultValue = "10") @Min(1) long extraLifetimeInMinutes) {
        ServerUpdate update = factory.create(serverName, downtimeInMinutes, extraLifetimeInMinutes);
        service.put(update);
        return ResponseEntityUtil.noContent();
    }

    @GetMapping
    public ResponseEntity<Instant> get(@RequestParam(name = "serverName") @NotBlank String serverName) {
        return service.get(serverName)
                .map(dateTime -> ResponseEntityUtil.ok(dateTime, UPDATE_DOWNTIME_CACHE_DURATION))
                .orElseGet(ResponseEntityUtil::noContent);
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam(name = "serverName") @NotBlank String serverName) {
        service.remove(serverName);
        return ResponseEntityUtil.noContent();
    }
}
