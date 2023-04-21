package ru.practicum.exception;

public class ExistRelatedDataException extends RuntimeException {
    public ExistRelatedDataException(String message) {
        super(message);
    }
}
