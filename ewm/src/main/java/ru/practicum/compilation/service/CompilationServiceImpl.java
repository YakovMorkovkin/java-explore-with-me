package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dao.CompilationRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.mapper.CompilationDtoMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.exception.notfound.CompilationNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationDtoMapper compilationDtoMapper;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        log.info("Add compilation {}", newCompilationDto.getTitle());
        Compilation compilation = compilationDtoMapper.toModel(newCompilationDto);
        compilation.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));
        return compilationDtoMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto getComp(long id) {
        log.info("Add compilation with id - {}", id);
        return compilationDtoMapper.toDto(getCheckedComp(id));
    }

    @Override
    public List<CompilationDto> getComps(Boolean pinned, Long from, Long size) {
        log.info("Add compilations with filters pinned - {}", pinned);
        return compilationRepository.findComps(pinned, from, size).stream()
                .map(compilationDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delComp(long compId) {
        log.info("Delete compilation with id - {}", compId);
        getCheckedComp(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long id, NewCompilationDto newCompilationDto) {
        log.info("Update compilation with id - {}", id);
        Compilation compilation = getCheckedComp(id);
        Compilation result = composeComp(compilation, newCompilationDto);
        result.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));
        return compilationDtoMapper.toDto(compilationRepository.save(result));
    }

    private Compilation getCheckedComp(Long id) {
        return compilationRepository.findById(id).orElseThrow(
                () -> new CompilationNotFoundException(id)
        );
    }

    private Compilation composeComp(Compilation compilation, NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .id(compilation.getId())
                .pinned(newCompilationDto.isPinned() != compilation.isPinned() ? newCompilationDto.isPinned() : compilation.isPinned())
                .title(newCompilationDto.getTitle() != null ? newCompilationDto.getTitle() : compilation.getTitle())
                .build();
    }
}
