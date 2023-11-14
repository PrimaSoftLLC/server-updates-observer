package by.aurorasoft.updatesobserver.controller;

import by.aurorasoft.updatesobserver.controller.model.ServerUpdateRequest;
import by.aurorasoft.updatesobserver.service.ServerUpdateService;
import by.aurorasoft.updatesobserver.service.factory.ServerUpdateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/serverUpdate")
@RequiredArgsConstructor
public class ServerUpdateController {
    private final ServerUpdateService durationService;
    private final ServerUpdateFactory

    @PostMapping
    public ResponseEntity<?> save(@RequestParam(name = "serverName") final String serverName,
                                  @RequestParam(name = "downtime") final int downtimeInMinutes,
                                  @RequestParam(name = "lifetime", defaultValue = "10") final int lifetimeInMinutes) {

    }
}
