package by.aurorasoft.outagetracker.controller;

import by.aurorasoft.outagetracker.service.ServerOutageService;
import by.aurorasoft.outagetracker.service.factory.ServerOutageFactory;
import by.aurorasoft.outagetracker.util.ResponseEntityUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.SECONDS;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/serverOutage")
public class ServerOutageController {
    private static final Duration UPDATE_DOWNTIME_CACHE_DURATION = Duration.of(30, SECONDS);

    private final ServerOutageFactory factory;
    private final ServerOutageService service;

    /**
     * Creates a server outage record.
     *
     * @param serverName             The name of the server.
     * @param downtimeMinutes      The downtime in minutes.
     * @param extraLifetimeMinutes Additional lifetime after downtime ends.
     * @return ResponseEntity indicating the operation's outcome.
     */
    @PostMapping
    public ResponseEntity<Void> create(@RequestParam(name = "serverName") @NotBlank String serverName,
                                       @RequestParam(name = "downtimeMinutes", defaultValue = "10") @Min(1) long downtimeMinutes,
                                       @RequestParam(name = "extraLifetimeMinutes", defaultValue = "10") @Min(1) long extraLifetimeMinutes) {
        var serverOutage = factory.create(serverName, downtimeMinutes, extraLifetimeMinutes);
        service.save(serverOutage);
        return ResponseEntityUtil.noContent();
    }

    /**
     * Retrieves the downtime details of a server.
     *
     * @param serverName The name of the server.
     * @return ResponseEntity containing the downtime details or no content.
     */
    @GetMapping
    public ResponseEntity<Instant> getDowntime(@RequestParam(name = "serverName") @NotBlank String serverName) {
        return service.getDowntime(serverName)
                .map(dateTime -> ResponseEntityUtil.ok(dateTime, UPDATE_DOWNTIME_CACHE_DURATION))
                .orElseGet(ResponseEntityUtil::noContent);
    }

    /**
     * Removes a server outage record.
     *
     * @param serverName The name of the server.
     * @return ResponseEntity indicating the operation's outcome.
     */
    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam(name = "serverName") @NotBlank String serverName) {
        service.remove(serverName);
        return ResponseEntityUtil.noContent();
    }
}
