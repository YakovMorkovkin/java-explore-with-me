package ru.practicum.event.dto.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.model.Location;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LocationMapper {
    Location toModel(LocationDto locationDto);

    LocationDto toDto(Location location);
}
