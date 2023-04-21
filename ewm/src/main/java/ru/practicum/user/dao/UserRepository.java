package ru.practicum.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT new ru.practicum.user.dto.UserDto(u.id, u.name, u.email) " +
            "FROM User u " +
            "WHERE u.id IN :ids " +
            "ORDER BY u.id")
    List<UserDto> findUsersByIds(List<Long> ids);

    @Query(nativeQuery = true)
    List<UserDto> findUsersFrom(Integer first, Integer size);
}

