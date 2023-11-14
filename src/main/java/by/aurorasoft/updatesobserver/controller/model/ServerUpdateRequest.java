package by.aurorasoft.updatesobserver.controller.model;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
public class ServerUpdateRequest {

    @NotNull
    String serverName;

    @NotNull
    @Positive
    Integer downtimeInMinutes;

    @NotNull
    @Positive
    Integer lifetimeInMinutes;
}
