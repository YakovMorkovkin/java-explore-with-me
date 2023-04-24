package ru.practicum;

import java.time.format.DateTimeFormatter;

public final class Constants {
    public static final String DT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Constants() {
    }
}
