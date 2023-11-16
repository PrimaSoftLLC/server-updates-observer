package by.aurorasoft.updatesobserver.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ControllerAdvice
public final class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final ConstraintViolationException exception) {
        return createResponseEntity(
                exception,
                Exception::getMessage,
                NOT_ACCEPTABLE
        );
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final Exception exception) {
        return createResponseEntity(
                exception,
                Exception::getMessage,
                INTERNAL_SERVER_ERROR
        );
    }

    private static <E extends Exception> ResponseEntity<RestErrorResponse> createResponseEntity(
            final E exception,
            final ExceptionMessageExtractor<E> messageExtractor,
            final HttpStatus httpStatus
    ) {
        final String message = messageExtractor.extract(exception);
        final RestErrorResponse errorResponse = createErrorResponse(message, httpStatus);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    private static RestErrorResponse createErrorResponse(final String message, final HttpStatus httpStatus) {
        final LocalDateTime currentDateTime = now();
        return new RestErrorResponse(httpStatus, message, currentDateTime);
    }

    private record RestErrorResponse(HttpStatus httpStatus,
                                     String message,
                                     @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH-mm-ss") LocalDateTime dateTime) {
    }

    @FunctionalInterface
    private interface ExceptionMessageExtractor<E extends Exception> {
        String extract(final E exception);
    }
}

