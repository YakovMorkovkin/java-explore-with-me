package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.category.dao.CategoryRepository;
import ru.practicum.enums.State;
import ru.practicum.enums.StateAction;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.dto.in.NewEventDto;
import ru.practicum.event.dto.in.UpdateEventDto;
import ru.practicum.event.dto.mapper.EventDtoMapper;
import ru.practicum.event.dto.mapper.LocationMapper;
import ru.practicum.event.dto.outside.EventFullDto;
import ru.practicum.event.dto.outside.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.exception.notfound.CategoryNotFoundException;
import ru.practicum.exception.notfound.EventNotFoundException;
import ru.practicum.exception.notfound.NotFoundException;
import ru.practicum.exception.time.ToEarlyEventStart1HException;
import ru.practicum.exception.time.ToEarlyEventStart2HException;
import ru.practicum.exception.unavailable.EventUnavailableException;
import ru.practicum.statsclient.StatsClient;
import ru.practicum.statsdto.HitDtoIn;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.Constants.DT_FORMATTER;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final EventDtoMapper eventDtoMapper;
    private final LocationMapper locationMapper;
    private final StatsClient statsClient;

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        log.info("Add event {}", newEventDto);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime;
        User initiator = userService.getUserCheked(userId);
        Event event = eventDtoMapper.toModel(newEventDto);
        event.setState(State.PENDING.name());
        startTime = now.plusHours(2);
        if (event.getEventDate().isAfter(startTime)) {
            event.setCreatedOn(now);
            event.setInitiator(initiator);
            event.setConfirmedRequests(0L);
            event.setViews(0L);
            return eventDtoMapper.toFullDto(eventRepository.save(event));
        } else throw new ToEarlyEventStart2HException(startTime);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId) {
        log.info("Get events of user with id - {}", userId);
        return eventRepository.findAllByInitiatorId(userId).stream().map(eventDtoMapper::toShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        log.info("Get event with id - {}", eventId);
        return eventDtoMapper.toFullDto(eventRepository.findByInitiatorIdAndId(userId, eventId));
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, Long from, Long size) {
        log.info("Get events by admin with filters users - {}, states - {}, categories - {}, rangeStart - {}, rangeEnd - {}",
                users, states, categories, rangeStart,rangeEnd);
        if (states != null) {
            return eventRepository.searchByAdmin(users, states, categories, parseStringToLocalDateTime(rangeStart),
                            parseStringToLocalDateTime(rangeEnd), from, size).stream()
                    .map(eventDtoMapper::toFullDto)
                    .collect(Collectors.toList());
        } else {
            return eventRepository.searchWithoutStateByAdmin(users, categories, parseStringToLocalDateTime(rangeStart),
                            parseStringToLocalDateTime(rangeEnd), from, size).stream()
                    .map(eventDtoMapper::toFullDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable, String sort, Long from, Long size,
                                               HttpServletRequest request) {
        log.info("Get events with filters text - {}, categories - {}, paid - {}, rangeStart - {}," +
                        " rangeEnd - {}, onlyAvailable - {}, sort - {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);

        statsClient.addStats(HitDtoIn.builder()
                .app("EWM")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(DT_FORMATTER))
                .build());
        return eventRepository.searchPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size).stream()
                .map(eventDtoMapper::toShortDto)
                .collect(Collectors.toList());

    }

    @Override
    public EventFullDto editEventByAdmin(Long eventId, UpdateEventDto updateEventDto) {
        log.info("Edit event with id - {} by admin", eventId);
        Event event = getEventChecked(eventId);
        Event editedEvent = composeEvent(updateEventDto, event);
        if (editedEvent.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ToEarlyEventStart1HException(editedEvent.getEventDate());
        } else if (updateEventDto.getStateAction() != null &&
                updateEventDto.getStateAction().equals(StateAction.PUBLISH_EVENT.name()) &&
                event.getState().equals(State.PENDING.name())) {
            editedEvent.setPublishedOn(LocalDateTime.now());
            return eventDtoMapper.toFullDto(eventRepository.save(editedEvent));
        } else if (updateEventDto.getStateAction() != null &&
                updateEventDto.getStateAction().equals(StateAction.REJECT_EVENT.name()) &&
                !event.getState().equals(State.PUBLISHED.name())) {
            return eventDtoMapper.toFullDto(eventRepository.save(composeEvent(updateEventDto, event)));
        } else throw new EventUnavailableException(eventId);
    }

    @Override
    public EventFullDto editEvent(Long userId, Long eventId, UpdateEventDto updateEventDto) {
        log.info("Edit event with id - {} by user(id - {})", eventId, userId);
        userService.getUserCheked(userId);
        Event event = getEventChecked(eventId);
        Event editedEvent = composeEvent(updateEventDto, event);
        if (editedEvent.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ToEarlyEventStart1HException(editedEvent.getEventDate());
        } else if (event.getInitiator().getId().equals(userId) && !event.getState().equals("PUBLISHED")) {
            return eventDtoMapper.toFullDto(eventRepository.save(composeEvent(updateEventDto, event)));
        } else throw new EventUnavailableException(eventId);

    }

    @Override
    public EventFullDto getEventPublic(Long id, HttpServletRequest request) {
        log.info("Get event with id - {})", id);
        Event event = getEventChecked(id);
        statsClient.addStats(HitDtoIn.builder()
                .app("EWM")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(DT_FORMATTER))
                .build());
        event.setViews(event.getViews() + 1L);
        return eventDtoMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public Event getEventChecked(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    @Override
    public Event updateEvent(Event event) {
        log.info("Update event with id - {})", event.getId());
        return eventRepository.save(event);
    }

    private Event composeEvent(UpdateEventDto updateEventDto, Event event) {
        return Event.builder()
                .id(event.getId())
                .annotation(updateEventDto.getAnnotation() != null ? updateEventDto.getAnnotation() : event.getAnnotation())
                .initiator(event.getInitiator())
                .confirmedRequests(event.getConfirmedRequests())
                .category(updateEventDto.getCategory() != null ? categoryRepository.findById(updateEventDto.getCategory())
                        .orElseThrow(() -> new CategoryNotFoundException(updateEventDto.getCategory())) : event.getCategory())
                .description(updateEventDto.getDescription() != null ? updateEventDto.getDescription() : event.getDescription())
                .eventDate(updateEventDto.getEventDate() != null ? LocalDateTime.parse(updateEventDto.getEventDate(),
                        DT_FORMATTER) : event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .location(updateEventDto.getLocation() != null ? locationMapper.toModel(updateEventDto.getLocation()) :
                        event.getLocation())
                .paid(updateEventDto.getPaid() != null ? updateEventDto.getPaid() : event.isPaid())
                .participantLimit(updateEventDto.getParticipantLimit() != null ? updateEventDto.getParticipantLimit() :
                        event.getParticipantLimit())
                .requestModeration(updateEventDto.getRequestModeration() != null &&
                        updateEventDto.getRequestModeration() != event.isRequestModeration() ?
                        updateEventDto.getRequestModeration() : event.isRequestModeration())
                .title(updateEventDto.getTitle() != null ? updateEventDto.getTitle() : event.getTitle())
                .state(updateEventDto.getStateAction() != null ?
                        stateActionToState(StateAction.valueOf(updateEventDto.getStateAction())) : event.getState())
                .views(event.getViews())
                .build();
    }

    private LocalDateTime parseStringToLocalDateTime(String stringTime) {
        if (stringTime != null) {
            return LocalDateTime.parse(stringTime, DT_FORMATTER);
        } else return null;
    }

    private String stateActionToState(StateAction stateAction) {
        switch (stateAction) {
            case CANCEL_REVIEW:
            case REJECT_EVENT:
                return State.CANCELED.name();
            case SEND_TO_REVIEW:
                return State.PENDING.name();
            case PUBLISH_EVENT:
                return State.PUBLISHED.name();
            default:
                throw new NotFoundException(String.format("Not found StateAction-%s", stateAction.name()));
        }
    }
}
