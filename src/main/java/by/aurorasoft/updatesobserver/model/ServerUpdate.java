package by.aurorasoft.updatesobserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Value
@AllArgsConstructor
@Builder
public class ServerUpdate implements Serializable {
    String serverName;
    Instant start;
    int downtimeInMinutes;
    int extraDowntimeInMinutes;
}
