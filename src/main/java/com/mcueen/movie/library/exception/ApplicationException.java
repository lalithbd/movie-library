package com.mcueen.movie.library.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApplicationException extends Exception {

    private HttpStatus httpStatus;
    private String message;

    public ApplicationException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
