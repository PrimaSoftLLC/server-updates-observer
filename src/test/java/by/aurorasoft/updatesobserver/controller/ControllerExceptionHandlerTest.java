package by.aurorasoft.updatesobserver.controller;

import jakarta.validation.ConstraintViolationException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.util.Collections.emptySet;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

public final class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler exceptionHandler = new ControllerExceptionHandler();

    @Test
    public void exceptionShouldBeHandled() {
        var givenDescription = "exception-description";
        var givenException = new Exception(givenDescription);

        ResponseEntity<?> actual = exceptionHandler.handleException(givenException);

        assertErrorResponse(givenDescription, INTERNAL_SERVER_ERROR, actual);
    }

    @Test
    public void constraintViolationExceptionShouldBeHandled() {
        var givenDescription = "exception-description";
        var givenException = new ConstraintViolationException(givenDescription, emptySet());

        ResponseEntity<?> actual = this.exceptionHandler.handleException(givenException);

        assertErrorResponse(givenDescription, NOT_ACCEPTABLE, actual);
    }

    private void assertErrorResponse(String expectedDescription, HttpStatus expectedHttpStatus, ResponseEntity<?> actual){
        assertSame(expectedHttpStatus, actual.getStatusCode());

        RestErrorResponse actualErrorResponse = (RestErrorResponse) actual.getBody();

        assert actualErrorResponse != null;
        assertSame(expectedHttpStatus, actualErrorResponse.getHttpStatus());
        assertSame(expectedDescription, actualErrorResponse.getMessage());
        assertNotNull(actualErrorResponse.getTime());
    }
}
