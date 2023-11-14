package by.aurorasoft.updatesobserver.controller.model;

import by.aurorasoft.updatesobserver.base.AbstractContextTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class ServerUpdateRequestTest extends AbstractContextTest {

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
    private Validator validator = null;

    @Test
    public void requestShouldBeMappedToJson()
            throws Exception {
        final ServerUpdateRequest givenRequest = new ServerUpdateRequest(
                "server-name",
                5,
                10
        );

        final String actual = this.objectMapper.writeValueAsString(givenRequest);
        final String expected = "{\"serverName\":\"server-name\",\"downtimeInMinutes\":5,\"lifetimeInMinutes\":10}";
        assertEquals(expected, actual);
    }

    @Test
    public void jsonShouldBeMappedToRequest()
            throws Exception {
        final String givenJson = "{\"serverName\":\"server-name\",\"downtimeInMinutes\":5,\"lifetimeInMinutes\":10}";

        final ServerUpdateRequest actual = this.objectMapper.readValue(givenJson, ServerUpdateRequest.class);
        final ServerUpdateRequest expected = new ServerUpdateRequest(
                "server-name",
                5,
                10
        );
        assertEquals(expected, actual);
    }

    @Test
    public void requestShouldBeValid() {
        final ServerUpdateRequest givenRequest = new ServerUpdateRequest(
                "server-name",
                5,
                10
        );

        final Set<ConstraintViolation<ServerUpdateRequest>> constraintViolations = this.validator.validate(givenRequest);
        assertTrue(constraintViolations.isEmpty());
    }
}
