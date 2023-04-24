package ru.practicum.user.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Service
@Validated
public interface UserService {

    UserDto addUser(@Valid UserDtoIn userDto);

    List<UserDto> getUsers(List<Long> ids, @PositiveOrZero Integer offset, @Positive Integer limit);

    void deleteUser(@Positive Long userId);

    User getUserCheked(@Positive Long userId);
}
