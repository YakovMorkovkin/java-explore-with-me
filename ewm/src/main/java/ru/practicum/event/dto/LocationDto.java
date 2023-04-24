package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
@Builder
public class LocationDto {
    @NotEmpty
    private float lat;
    @NotEmpty
    private float lon;
}
