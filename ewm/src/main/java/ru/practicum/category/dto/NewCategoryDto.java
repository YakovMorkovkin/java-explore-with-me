package ru.practicum.category.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewCategoryDto {
    @NotEmpty
    private String name;
}
