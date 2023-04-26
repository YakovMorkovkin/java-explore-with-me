package ru.practicum.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class NewCommentDto {
    @NotNull
    private String text;
    @NotNull
    private Long eventId;
}
