package ru.practicum.event.dto.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.category.dto.mapper.CategoryDtoMapper;
import ru.practicum.event.dto.in.NewEventDto;
import ru.practicum.event.dto.outside.EventFullDto;
import ru.practicum.event.dto.outside.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.mapper.UserDtoMapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {UserDtoMapper.class, CategoryDtoMapper.class})
public interface EventDtoMapper {
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "category", target = "category.id")
    Event toModel(NewEventDto newEventDto);

    @Mapping(source = "location", target = "location")
    @Mapping(source = "category", target = "category")
    EventFullDto toFullDto(Event event);

    @Mapping(source = "category", target = "category")
    EventShortDto toShortDto(Event event);
}
