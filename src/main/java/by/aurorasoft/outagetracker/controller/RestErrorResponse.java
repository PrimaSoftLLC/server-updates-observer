package by.aurorasoft.outagetracker.controller;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Value
@FieldNameConstants
@SuppressWarnings("ClassCanBeRecord")
public class RestErrorResponse {
    HttpStatus httpStatus;
    String message;
    Instant time;
}
