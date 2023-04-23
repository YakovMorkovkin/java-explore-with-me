package ru.practicum.exception.time;

import java.time.LocalDateTime;

import static ru.practicum.Constants.DT_FORMATTER;

public class ToEarlyEventStart1HException extends RuntimeException {
    public ToEarlyEventStart1HException(LocalDateTime startTime) {
        super(String.format("Event should start no earlier than %s ",
                startTime.plusHours(1).format(DT_FORMATTER)));
    }
}
