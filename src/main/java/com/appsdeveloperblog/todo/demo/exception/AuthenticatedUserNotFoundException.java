package com.appsdeveloperblog.todo.demo.exception;

public class AuthenticatedUserNotFoundException extends RuntimeException {

    public AuthenticatedUserNotFoundException(final String message) {
        super(message);
    }
}
