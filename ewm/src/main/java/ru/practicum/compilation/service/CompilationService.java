package ru.practicum.compilation.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public interface CompilationService {

    CompilationDto addCompilation(@Valid NewCompilationDto newCompilationDto);

    CompilationDto getComp(long id);

    List<CompilationDto> getComps(Boolean pinned, Long from, Long size);

    void delComp(long compId);

    CompilationDto updateCompilation(Long id, NewCompilationDto newCompilationDto);
}


