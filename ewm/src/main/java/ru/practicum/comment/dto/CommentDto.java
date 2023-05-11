package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.Constants.DT_PATTERN;

@Data
@Builder
public class CommentDto {
    private Long id;
    private UserShortDto author;
    private Long eventId;
    private String text;
    private String state;
    @JsonFormat(pattern = DT_PATTERN)
    private LocalDateTime created;
}
