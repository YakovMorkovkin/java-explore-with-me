package ru.practicum.exception.notfound;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long itemId) {
        super(String.format("Category with id - %d not found", itemId));
    }
}

