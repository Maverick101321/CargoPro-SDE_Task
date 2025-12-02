package com.cargopro.tms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a transporter does not have sufficient capacity
 * to fulfill a bid or booking.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientCapacityException extends RuntimeException {
    public InsufficientCapacityException(String message) {
        super(message);
    }
}
