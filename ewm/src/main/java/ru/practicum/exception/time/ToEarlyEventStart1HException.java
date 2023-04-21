package ru.practicum.exception.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ToEarlyEventStart1HException extends RuntimeException {
    public ToEarlyEventStart1HException(LocalDateTime startTime) {
        super(String.format("Event should start no earlier than %s ",
                startTime.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }
}
