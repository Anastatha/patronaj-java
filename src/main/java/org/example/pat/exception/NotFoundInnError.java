package org.example.pat.exception;

public class NotFoundInnError extends RuntimeException {
    public NotFoundInnError() {
        super("ИНН не найден");
    }
}
