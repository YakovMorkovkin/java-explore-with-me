package ru.practicum.participationrequest.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.event.dto.in.NewRequestsStatusDto;
import ru.practicum.event.dto.outside.RequestsStatusDto;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;

import java.util.List;

@Service
@Validated
public interface ParticipationRequestService {
    List<ParticipationRequestDto> getRequests(Long userId);

    List<ParticipationRequestDto> getRequestsEventForInitiator(Long userId, Long eventId);

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    RequestsStatusDto changeStatus(Long userId, Long eventId, NewRequestsStatusDto newRequestsStatusDto);
}
