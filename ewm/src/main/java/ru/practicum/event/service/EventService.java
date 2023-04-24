package ru.practicum.event.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.event.dto.in.NewEventDto;
import ru.practicum.event.dto.in.UpdateEventDto;
import ru.practicum.event.dto.outside.EventFullDto;
import ru.practicum.event.dto.outside.EventShortDto;
import ru.practicum.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Service
@Validated
public interface EventService {
    EventFullDto addEvent(@Positive Long userId, @Valid NewEventDto newEventDto);

    List<EventShortDto> getEvents(@Positive Long userId);

    EventFullDto getEvent(@Positive Long userId, @Positive Long eventId);

    List<EventFullDto> getEventsByAdmin(List<Long> users,
                                        List<String> states,
                                        List<Long> categories,
                                        String rangeStart,
                                        String rangeEnd,
                                        Long from,
                                        Long size);

    List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid, String rangeStart,
                                        String rangeEnd, Boolean onlyAvailable, String sort, Long from, Long size,
                                        HttpServletRequest request);

    EventFullDto editEventByAdmin(@Positive Long eventId, UpdateEventDto updateEventDto);

    Event getEventChecked(@Positive Long eventId);

    EventFullDto editEvent(@Positive Long userId, @Positive Long eventId, UpdateEventDto updateEventDto);

    Event updateEvent(Event event);

    EventFullDto getEventPublic(Long id, HttpServletRequest request);

}
