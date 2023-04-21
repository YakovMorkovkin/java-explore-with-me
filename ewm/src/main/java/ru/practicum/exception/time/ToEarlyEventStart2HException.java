package ru.practicum.exception.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ToEarlyEventStart2HException extends RuntimeException {
    public ToEarlyEventStart2HException(LocalDateTime startTime) {
        super(String.format("Event should start no earlier than %s ",
                startTime.plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }
}
