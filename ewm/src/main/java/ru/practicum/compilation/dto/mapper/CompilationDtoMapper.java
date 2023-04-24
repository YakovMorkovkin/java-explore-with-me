package ru.practicum.compilation.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.mapper.EventDtoMapper;
import ru.practicum.event.model.Event;

@Mapper(componentModel = "spring", uses = {EventDtoMapper.class, Event.class})
public interface CompilationDtoMapper {
    @Mapping(source = "id", target = "id")
    Event toEvent(Long id);

    Compilation toModel(NewCompilationDto newCompilationDto);

    CompilationDto toDto(Compilation compilation);
}
