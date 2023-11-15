package by.aurorasoft.updatesobserver.service.savingupdate;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public final class ServerUpdateJsonSerializer {
    private final ObjectMapper objectMapper;

    public ServerUpdateJsonSerializer(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String serialize(final Collection<ServerUpdate> updates) {
        try {
            return this.objectMapper.writeValueAsString(updates);
        } catch (final JsonProcessingException cause) {
            throw new ServerUpdateJsonSerializationException(cause);
        }
    }

    static final class ServerUpdateJsonSerializationException extends RuntimeException {

        @SuppressWarnings("unused")
        public ServerUpdateJsonSerializationException() {

        }

        @SuppressWarnings("unused")
        public ServerUpdateJsonSerializationException(final String description) {
            super(description);
        }

        public ServerUpdateJsonSerializationException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ServerUpdateJsonSerializationException(final String description, final Exception cause) {
            super(description, cause);
        }

    }
}
