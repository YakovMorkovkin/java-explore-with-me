package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.in.NewEventDto;
import ru.practicum.event.dto.in.NewRequestsStatusDto;
import ru.practicum.event.dto.in.UpdateEventDto;
import ru.practicum.event.dto.out.EventFullDto;
import ru.practicum.event.dto.out.EventShortDto;
import ru.practicum.event.dto.out.RequestsStatusDto;
import ru.practicum.event.service.EventService;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.participationrequest.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventControllerPrivate {

    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId) {
        return eventService.getEvents(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId, @RequestBody NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto editEvent(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody UpdateEventDto updateEventDto) {
        return eventService.editEvent(userId, eventId, updateEventDto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getEventsRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return participationRequestService.getRequestsEventForInitiator(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestsStatusDto editEventsRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                @RequestBody NewRequestsStatusDto newRequestsStatusDto) {
        return participationRequestService.changeStatus(userId, eventId, newRequestsStatusDto);
    }
}
