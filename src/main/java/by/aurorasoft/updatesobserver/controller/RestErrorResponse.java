package by.aurorasoft.updatesobserver.controller;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Value
@FieldNameConstants
public class RestErrorResponse {
    HttpStatus httpStatus;
    String message;
    Instant time;
}
