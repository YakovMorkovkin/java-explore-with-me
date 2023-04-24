package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDtoIn {
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;

}
