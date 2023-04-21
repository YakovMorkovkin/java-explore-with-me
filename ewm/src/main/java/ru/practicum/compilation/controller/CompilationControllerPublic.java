package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getComps(@RequestParam(required = false) Boolean pinned,
                                         @RequestParam(defaultValue = "0", required = false) Long from,
                                         @RequestParam(defaultValue = "10", required = false) Long size) {
        return compilationService.getComps(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getComp(@PathVariable long compId) {
        return compilationService.getComp(compId);
    }
}
