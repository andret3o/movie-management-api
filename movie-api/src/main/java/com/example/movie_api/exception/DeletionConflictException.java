package com.example.movie_api.exception;

public class DeletionConflictException extends RuntimeException {
    public DeletionConflictException(String message){
        super(message);
    }
}
