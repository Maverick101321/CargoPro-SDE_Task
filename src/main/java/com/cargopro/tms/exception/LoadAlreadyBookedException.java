package com.cargopro.tms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an optimistic locking failure occurs, indicating
 * that the load has already been modified or booked by another transaction.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class LoadAlreadyBookedException extends RuntimeException {
    public LoadAlreadyBookedException(String message) {
        super(message);
    }
}
