package com.example.movie_api.exception;

public class DatabaseNotEmptyException extends RuntimeException {
    public DatabaseNotEmptyException(String message) {
        super(message);
    }
}

