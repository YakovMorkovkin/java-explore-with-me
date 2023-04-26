package ru.practicum.exception.notfound;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long categoryId) {
        super(String.format("Category with id - %d not found", categoryId));
    }
}

