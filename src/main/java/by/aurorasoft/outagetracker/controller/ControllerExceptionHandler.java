package by.aurorasoft.outagetracker.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ControllerAdvice
public final class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(ConstraintViolationException exception) {
        return createResponseEntity(
                exception,
                Exception::getMessage,
                NOT_ACCEPTABLE
        );
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(Exception exception) {
        return createResponseEntity(
                exception,
                Exception::getMessage,
                INTERNAL_SERVER_ERROR
        );
    }

    private static <E extends Exception> ResponseEntity<RestErrorResponse> createResponseEntity(
            E exception,
            ExceptionMessageExtractor<E> messageExtractor,
            HttpStatus httpStatus) {
        exception.printStackTrace();
        String message = messageExtractor.extract(exception);
        RestErrorResponse errorResponse = createErrorResponse(message, httpStatus);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    private static RestErrorResponse createErrorResponse(String message, HttpStatus httpStatus) {
        Instant currentDateTime = Instant.now();
        return new RestErrorResponse(httpStatus, message, currentDateTime);
    }

    @FunctionalInterface
    private interface ExceptionMessageExtractor<E extends Exception> {
        String extract(final E exception);
    }
}

