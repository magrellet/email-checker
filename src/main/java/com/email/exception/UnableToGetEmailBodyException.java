package com.email.exception;

public class UnableToGetEmailBodyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnableToGetEmailBodyException(String message, Throwable cause) {
        super(message, cause);
    }

}
