package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.exception.notfound.UserNotFoundException;
import ru.practicum.user.dao.UserRepository;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.dto.mapper.UserDtoMapper;
import ru.practicum.user.model.User;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;


    @Override
    public UserDto addUser(UserDtoIn userDtoIn) {
        return userDtoMapper.toDto(userRepository.save(userDtoMapper.toModel(userDtoIn)));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer offset, Integer limit) {
        if (ids != null) {
            return userRepository.findUsersByIds(ids);
        } else return userRepository.findUsersFrom(offset, limit);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserCheked(userId);
        userRepository.deleteById(user.getId());
    }

    @Override
    public User getUserCheked(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
