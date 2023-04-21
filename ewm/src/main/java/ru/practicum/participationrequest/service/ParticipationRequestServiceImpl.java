package ru.practicum.participationrequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.event.dto.in.NewRequestsStatusDto;
import ru.practicum.event.dto.outside.RequestsStatusDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.duplicate.RequestDuplicateException;
import ru.practicum.exception.notfound.RequestNotFoundException;
import ru.practicum.exception.unavailable.EventUnavailableException;
import ru.practicum.exception.unavailable.UnavailableException;
import ru.practicum.participationrequest.dao.ParticipationRequestRepository;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.participationrequest.dto.mapper.ParticipationRequestDtoMapper;
import ru.practicum.participationrequest.model.ParticipationRequest;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Primary
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final ParticipationRequestDtoMapper participationRequestDtoMapper;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        return participationRequestRepository.findAllByRequesterId(userId).stream()
                .map(participationRequestDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getRequestsEventForInitiator(Long userId, Long eventId) {
        if (eventService.getEventChecked(eventId).getInitiator().getId().equals(userId)) {
            return participationRequestRepository.findByEventId(eventId)
                    .stream()
                    .map(participationRequestDtoMapper::toDto)
                    .collect(Collectors.toList());
        } else throw new UnavailableException("User isn't initiator of this event");
    }

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        Optional<ParticipationRequest> request = participationRequestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (request.isPresent()) {
            throw new RequestDuplicateException(request.get().getId());
        } else {
            User user = userService.getUserCheked(userId);
            Event event = eventService.getEventChecked(eventId);
            if (!event.getInitiator().getId().equals(userId) && event.getState().equals(State.PUBLISHED.name()) &&
                    event.getParticipantLimit() > event.getConfirmedRequests()
                    && event.isRequestModeration()) {
                return participationRequestDtoMapper.toDto(
                        participationRequestRepository.save(
                                ParticipationRequest.builder()
                                        .created(LocalDateTime.now())
                                        .requester(user)
                                        .event(event)
                                        .status(Status.PENDING.name())
                                        .build()
                        )
                );
            } else if (!event.getInitiator().getId().equals(userId) && event.getState().equals(State.PUBLISHED.name())
                    && event.getParticipantLimit() > event.getConfirmedRequests()) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventService.updateEvent(event);
                return participationRequestDtoMapper.toDto(
                        participationRequestRepository.save(
                                ParticipationRequest.builder()
                                        .created(LocalDateTime.now())
                                        .requester(user)
                                        .event(event)
                                        .status(Status.CONFIRMED.name())
                                        .build()
                        )
                );
            } else throw new EventUnavailableException(userId);
        }
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest participationRequest = getParticipationRequestChecked(requestId);
        if (participationRequest.getRequester().getId().equals(userId)) {
            participationRequest.setStatus(State.CANCELED.name());
            return participationRequestDtoMapper.toDto(participationRequestRepository.save(participationRequest));
        } else throw new UnavailableException("Request unavailable");
    }

    @Override
    public RequestsStatusDto changeStatus(Long userId, Long eventId, NewRequestsStatusDto newRequestsStatusDto) {
        RequestsStatusDto requestsStatusDto = RequestsStatusDto.builder().build();
        Event event = eventService.getEventChecked(eventId);
        List<ParticipationRequest> confirmedRqsts = new ArrayList<>();
        List<ParticipationRequest> rejectedRqsts;
        List<ParticipationRequest> requests = participationRequestRepository.findAllById(newRequestsStatusDto.getRequestIds());
        if (event.getParticipantLimit().equals(0L) || !event.isRequestModeration()) {
            requestsStatusDto.setConfirmedRequests(new ArrayList<>());
            requestsStatusDto.setRejectedRequests(new ArrayList<>());
            return requestsStatusDto;
        } else if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new EventUnavailableException(event.getId());
        } else if (event.getInitiator().getId().equals(userId)) {
            Long counter = event.getConfirmedRequests();

            if (newRequestsStatusDto.getStatus().equals(Status.CONFIRMED.name())) {
                for (int i = 0; i < requests.size() && counter <= event.getParticipantLimit(); i++) {
                    if (requests.get(i).getStatus().equals(State.PENDING.name())) {
                        requests.get(i).setStatus(Status.CONFIRMED.name());
                        confirmedRqsts.add(requests.get(i));
                        ++counter;
                    } else {
                        throw new UnavailableException("Only pending request can be confirmed");
                    }
                }
                rejectedRqsts = requests.stream()
                        .filter(x -> !confirmedRqsts.contains(x))
                        .peek(x -> x.setStatus(Status.REJECTED.name()))
                        .collect(Collectors.toList());
            } else rejectedRqsts = requests.stream()
                    .peek(x -> x.setStatus(Status.REJECTED.name()))
                    .collect(Collectors.toList());

            if (counter.equals(event.getParticipantLimit())) {
                event.setConfirmedRequests(counter);
            }
            participationRequestRepository.saveAll(
                    Stream.concat(confirmedRqsts.stream(), rejectedRqsts.stream())
                            .collect(Collectors.toList())
            );

            requestsStatusDto.setConfirmedRequests(
                    confirmedRqsts.stream()
                            .map(participationRequestDtoMapper::toDto)
                            .collect(Collectors.toList())
            );
            requestsStatusDto.setRejectedRequests(
                    rejectedRqsts.stream()
                            .map(participationRequestDtoMapper::toDto)
                            .collect(Collectors.toList())
            );
        } else throw new UnavailableException("User isn't initiator of this event");
        return requestsStatusDto;
    }

    public ParticipationRequest getParticipationRequestChecked(Long requestId) {
        return participationRequestRepository.findById(
                requestId).orElseThrow(() -> new RequestNotFoundException(requestId)
        );
    }
}
