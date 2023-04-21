package ru.practicum.event.dto.outside;

import lombok.Builder;
import lombok.Data;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;

import java.util.List;

@Data
@Builder
public class RequestsStatusDto {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
