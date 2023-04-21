package ru.practicum.exception;

import ru.practicum.exception.unavailable.UnavailableException;

public class UnsupportedStatusException extends UnavailableException {
    public UnsupportedStatusException(String status) {
        super(String.format("Unknown state: %s", status));
    }
}
