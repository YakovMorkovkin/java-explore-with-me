package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class NewCompilationDto {
    @NotNull
    private List<Long> events;
    @NotNull
    private boolean pinned;
    @NotNull
    private String title;
}
