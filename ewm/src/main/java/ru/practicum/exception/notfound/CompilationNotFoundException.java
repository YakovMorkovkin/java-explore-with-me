package ru.practicum.exception.notfound;

public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(Long compId) {
        super(String.format("Compilation with id - %d not found", compId));
    }
}
