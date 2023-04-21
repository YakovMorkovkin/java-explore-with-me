package ru.practicum.user.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    User toModel(UserDtoIn userDtoIn);

    UserDto toDto(User model);

    UserShortDto toShortDto(User model);
}
