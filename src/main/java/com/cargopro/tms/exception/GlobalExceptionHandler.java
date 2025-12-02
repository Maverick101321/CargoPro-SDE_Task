package com.cargopro.tms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler to catch and process exceptions across the application.
 * Ensures consistent error responses for the API.
 */
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException.
     * Returns 404 NOT FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InvalidStatusTransitionException.
     * Returns 400 BAD REQUEST.
     */
    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStatusTransitionException(InvalidStatusTransitionException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InsufficientCapacityException.
     * Returns 400 BAD REQUEST.
     */
    @ExceptionHandler(InsufficientCapacityException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientCapacityException(InsufficientCapacityException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles LoadAlreadyBookedException.
     * Returns 409 CONFLICT.
     * This is critical for handling optimistic locking failures where concurrent bookings occur.
     */
    @ExceptionHandler(LoadAlreadyBookedException.class)
    public ResponseEntity<Map<String, Object>> handleLoadAlreadyBookedException(LoadAlreadyBookedException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Helper method to build a structured error response.
     *
     * @param message The error message.
     * @param status  The HTTP status code.
     * @return A ResponseEntity containing the error details.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
