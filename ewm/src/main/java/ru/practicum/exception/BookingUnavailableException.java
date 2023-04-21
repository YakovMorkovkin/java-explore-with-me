package ru.practicum.exception;

import ru.practicum.exception.notfound.NotFoundException;

public class BookingUnavailableException extends NotFoundException {
    public BookingUnavailableException(String message) {
        super(message);
    }
}
