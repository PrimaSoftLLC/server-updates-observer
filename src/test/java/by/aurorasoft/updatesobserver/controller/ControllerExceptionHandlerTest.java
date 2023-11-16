package by.aurorasoft.updatesobserver.controller;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static by.aurorasoft.updatesobserver.util.ReflectionUtil.findProperty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public final class ControllerExceptionHandlerTest {
    private static final String FIELD_NAME_ERROR_RESPONSE_HTTP_STATUS = "httpStatus";
    private static final String FIELD_NAME_ERROR_RESPONSE_MESSAGE = "message";
    private static final String FIELD_NAME_ERROR_RESPONSE_DATE_TIME = "dateTime";

    private final ControllerExceptionHandler exceptionHandler = new ControllerExceptionHandler();

    @Test
    public void exceptionShouldBeHandled() {
        final String givenDescription = "exception-description";
        final Exception givenException = new Exception(givenDescription);

        final ResponseEntity<?> actual = this.exceptionHandler.handleException(givenException);
        final HttpStatus expectedHttpStatus = INTERNAL_SERVER_ERROR;
        assertSame(expectedHttpStatus, actual.getStatusCode());

        final Object actualErrorResponse = actual.getBody();

        final HttpStatus actualErrorResponseHttpStatus = findErrorResponseHttpStatus(actualErrorResponse);
        assertSame(expectedHttpStatus, actualErrorResponseHttpStatus);

        final String actualErrorResponseMessage = findErrorResponseMessage(actualErrorResponse);
        assertSame(givenDescription, actualErrorResponseMessage);

        final LocalDateTime actualErrorResponseDateTime = findErrorResponseDateTime(actualErrorResponse);
        assertNotNull(actualErrorResponseDateTime);
    }

    private static HttpStatus findErrorResponseHttpStatus(final Object errorResponse) {
        return findProperty(
                errorResponse,
                FIELD_NAME_ERROR_RESPONSE_HTTP_STATUS,
                HttpStatus.class
        );
    }

    private static String findErrorResponseMessage(final Object errorResponse) {
        return findProperty(
                errorResponse,
                FIELD_NAME_ERROR_RESPONSE_MESSAGE,
                String.class
        );
    }

    private static LocalDateTime findErrorResponseDateTime(final Object errorResponse) {
        return findProperty(
                errorResponse,
                FIELD_NAME_ERROR_RESPONSE_DATE_TIME,
                LocalDateTime.class
        );
    }
}
