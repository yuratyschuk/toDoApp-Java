package com.example.demo.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}