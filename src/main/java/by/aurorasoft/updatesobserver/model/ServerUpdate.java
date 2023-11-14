package by.aurorasoft.updatesobserver.model;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Value
public class ServerUpdate implements Serializable {
    String serverName;
    Instant start;
    int downtimeInMinutes;
    int lifetimeInMinutes;
}
