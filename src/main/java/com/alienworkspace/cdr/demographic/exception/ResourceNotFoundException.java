package com.alienworkspace.cdr.demographic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a resource is not found.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construct a new instance with a message that is a string concatenation of the field name and
     * the value of the field that was not found.
     *
     * @param message the error message
     */
    public ResourceNotFoundException(String message) {
        super(String.format("ResourceNotFoundException: %s ", message));
    }
}
