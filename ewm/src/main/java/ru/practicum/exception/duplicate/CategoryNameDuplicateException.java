package ru.practicum.exception.duplicate;

public class CategoryNameDuplicateException extends RuntimeException {
    public CategoryNameDuplicateException(String name) {
        super(String.format("Category with name - %s already exists", name));
    }
}

