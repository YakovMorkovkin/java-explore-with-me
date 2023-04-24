package ru.practicum.exception.notfound;

public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(Long requestId) {
        super(String.format("Participation request with id=%d not found", requestId));
    }
}