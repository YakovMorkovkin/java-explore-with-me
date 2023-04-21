package ru.practicum.exception.notfound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long userId) {
        super(String.format("User with id=%d not found", userId));
    }
}
