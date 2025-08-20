package org.example.pat.exception;

public class NotFoundOkvedError extends RuntimeException {
    public NotFoundOkvedError() {
        super("ОКВЭД не найден");
    }
}