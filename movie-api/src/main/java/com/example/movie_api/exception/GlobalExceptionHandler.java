package com.example.movie_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DatabaseNotEmptyException.class)
    public ResponseEntity<String> handleDatabaseNotEmpty(DatabaseNotEmptyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        String message = ex.getMessage();
        
        // Extract a more user-friendly error message
        if (message != null && message.contains("LocalDate")) {
            error.put("error", "Invalid date format. Expected format: yyyy-MM-dd (e.g., 1990-01-15)");
        } else if (message != null && message.contains("JSON parse error")) {
            error.put("error", "Invalid request body format: " + extractFieldFromMessage(message));
        } else {
            error.put("error", "Invalid request body: " + (message != null ? message : "Unable to parse request"));
        }
        
        return ResponseEntity.badRequest().body(error);
    }

    private String extractFieldFromMessage(String message) {
        // Try to extract field name from common error patterns
        if (message.contains("birthDate") || message.contains("birth_date")) {
            return "Invalid birthDate format. Expected format: yyyy-MM-dd";
        }
        return "Invalid format";
    }

    // Catch all other exceptions (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
