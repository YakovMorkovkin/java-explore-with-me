package ru.practicum.exception.duplicate;

public class RequestDuplicateException extends RuntimeException {
    public RequestDuplicateException(Long requestId) {
        super(String.format("Request with id - %d already exists", requestId));
    }
}