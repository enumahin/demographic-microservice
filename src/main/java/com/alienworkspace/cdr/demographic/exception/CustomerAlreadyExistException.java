package com.alienworkspace.cdr.demographic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a customer already exists.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerAlreadyExistException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CustomerAlreadyExistException(String message) {
        super(message);
    }
}
