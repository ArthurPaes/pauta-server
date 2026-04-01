package com.sicredi.pauta.exception;

import com.sicredi.pauta.infra.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // handles @Valid failures — returns a 400 with the first field error message
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String description = request != null ? request.getDescription(false) : "Unknown request";
        return new ErrorResponse(ex.getBindingResult().getFieldError().getDefaultMessage(), "VALIDATION_ERROR", description);
    }

    // handles business rule violations thrown as IllegalArgumentException — returns 400
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        String description = request != null ? request.getDescription(false) : "Unknown request";
        log.warn("IllegalArgumentException: {} - Request: {}", e.getMessage(), description);
        return new ErrorResponse(e.getMessage(), "VALIDATION_ERROR", description);
    }

    // handles failed login attempts — returns 401
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(AuthenticationException e, WebRequest request) {
        String description = request != null ? request.getDescription(false) : "Unknown request";
        log.warn("AuthenticationException: {} - Request: {}", e.getMessage(), description);
        return new ErrorResponse(e.getMessage(), "AUTHENTICATION_ERROR", description);
    }

    // catch-all for unexpected errors — returns 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception e, WebRequest request) {
        String description = request != null ? request.getDescription(false) : "Unknown request";
        log.error("Unhandled exception: {} - Request: {}", e.getMessage(), description, e);
        return new ErrorResponse("Erro interno do servidor", "INTERNAL_ERROR", description);
    }
}
