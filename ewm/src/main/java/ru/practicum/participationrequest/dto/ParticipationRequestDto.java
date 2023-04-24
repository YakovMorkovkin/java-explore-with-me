package ru.practicum.participationrequest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

import static ru.practicum.Constants.DT_PATTERN;

@Data
@ToString
@Builder
public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(pattern = DT_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private String status;
}
