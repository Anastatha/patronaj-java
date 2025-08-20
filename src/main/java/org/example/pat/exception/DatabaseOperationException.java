package org.example.pat.exception;

import org.springframework.http.HttpStatus;

public class DatabaseOperationException extends RuntimeException {
    private final HttpStatus status;

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
