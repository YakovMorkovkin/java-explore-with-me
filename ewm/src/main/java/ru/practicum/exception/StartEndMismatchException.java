package ru.practicum.exception;

import ru.practicum.exception.unavailable.UnavailableException;

import java.time.LocalDateTime;

public class StartEndMismatchException extends UnavailableException {
    public StartEndMismatchException(LocalDateTime date) {
        super("Start or End is null or start date of booking after end date - " + date);
    }
}
