package org.example.pat.exception;

import org.example.pat.dto.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Обработка валидации (@Valid, @Email, и т.п.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<?>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResult.Error<>("Ошибка валидации: " + errors));
    }

    // Обработка всех RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResult<?>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResult.Error<>(ex.getMessage()));
    }


    // Обработка NotFoundException
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResult<?>> handleNotFound(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResult.Error<>(ex.getMessage()));
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseException(DatabaseOperationException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                Map.of(
                        "error", "Ошибка базы данных",
                        "message", ex.getMessage(),
                        "status", ex.getStatus().value()
                )
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResult<?>> handleUserExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResult.Error<>(ex.getMessage()));
    }

    @ExceptionHandler({NotFoundInnError.class, NotFoundOkvedError.class})
    public ResponseEntity<ApiResult<?>> handleDadataNotFound(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResult.Error<>(ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenError.class)
    public ResponseEntity<ApiResult<?>> handleForbidden(ForbiddenError ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiResult.Error<>(ex.getMessage()));
    }


}

