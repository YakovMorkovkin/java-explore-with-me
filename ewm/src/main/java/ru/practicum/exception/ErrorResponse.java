package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.DT_PATTERN;

@Getter
public class ErrorResponse {
    private List<String> errors;
    private final String status;
    private final String reason;
    private final String message;
    @DateTimeFormat(pattern = DT_PATTERN)
    @JsonFormat(pattern = DT_PATTERN)
    private final LocalDateTime timestamp;

    public ErrorResponse(List<String> errors, String status, String reason, String message, LocalDateTime timestamp) {
        this.errors = errors;
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(String status, String reason, String message, LocalDateTime timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }


}

