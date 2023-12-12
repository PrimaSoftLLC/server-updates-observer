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
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static by.aurorasoft.updatesobserver.util.ResponseEntityUtil.noContent;
import static by.aurorasoft.updatesobserver.util.ResponseEntityUtil.ok;
import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequestMapping("/serverUpdate")
@RequiredArgsConstructor
@Validated
public class ServerUpdateController {
    private static final Duration UPDATE_DOWNTIME_CACHE_DURATION = Duration.of(1, DAYS);

    private final ServerUpdateFactory updateFactory;
    private final ServerUpdateService updateService;

    @PostMapping
    public ResponseEntity<?> createAndSaveIfAlive(@RequestParam(name = "serverName") @NotBlank final String serverName,
                                                  @RequestParam(name = "downtime") @Min(1) final long downtimeInMinutes,
                                                  @RequestParam(name = "extraLifetime", defaultValue = "10") @Min(1) final long extraLifetimeInMinutes) {
        final ServerUpdate update = this.updateFactory.create(serverName, downtimeInMinutes, extraLifetimeInMinutes);
        this.updateService.saveIfAlive(update);
        return noContent();
    }

    @GetMapping
    public ResponseEntity<Instant> findUpdateDowntime(@RequestParam(name = "serverName") @NotBlank final String serverName) {
        return this.findObjectByServerName(
                serverName,
                ServerUpdateService::findUpdateDowntime,
                dateTime -> ok(dateTime, UPDATE_DOWNTIME_CACHE_DURATION)
        );
    }

    @DeleteMapping
    public ResponseEntity<ServerUpdate> removeByServerName(@RequestParam(name = "serverName") @NotBlank final String serverName) {
        return this.findObjectByServerName(
                serverName,
                ServerUpdateService::removeByServerName,
                ResponseEntityUtil::ok
        );
    }

    private <T> ResponseEntity<T> findObjectByServerName(final String serverName,
                                                         final BiFunction<ServerUpdateService, String, Optional<T>> founder,
                                                         final Function<T, ResponseEntity<T>> responseEntityFactory) {
        return founder.apply(this.updateService, serverName)
                .map(responseEntityFactory)
                .orElseGet(ResponseEntityUtil::noContent);
    }
}
