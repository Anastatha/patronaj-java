package org.example.pat.exception;

public class ForbiddenError extends RuntimeException {
    public ForbiddenError(String message) {
        super(message);
    }
}