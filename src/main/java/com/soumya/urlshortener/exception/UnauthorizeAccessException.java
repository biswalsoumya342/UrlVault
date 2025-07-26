package com.soumya.urlshortener.exception;

public class UnauthorizeAccessException extends RuntimeException {

    public UnauthorizeAccessException(String message) {
        super(message);
    }
}
