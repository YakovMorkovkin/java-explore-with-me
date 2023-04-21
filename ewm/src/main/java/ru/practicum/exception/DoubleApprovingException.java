package ru.practicum.exception;

import ru.practicum.exception.unavailable.UnavailableException;

public class DoubleApprovingException extends UnavailableException {
    public DoubleApprovingException(Long bookingId) {
        super(String.format("Booking with id-%d is already approved", bookingId));
    }
}
