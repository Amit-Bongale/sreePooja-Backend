package com.example.sreepooja.ExceptionHandlers;

public class InvalidFileException extends RuntimeException {

    public InvalidFileException(String message) {
        super(message);
    }
}