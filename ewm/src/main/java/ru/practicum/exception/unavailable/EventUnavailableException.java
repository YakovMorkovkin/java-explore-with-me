package ru.practicum.exception.unavailable;

public class EventUnavailableException extends UnavailableException {
    public EventUnavailableException(Long eventId) {
        super(String.format("Event with id - %d unavailable", eventId));
    }
}
