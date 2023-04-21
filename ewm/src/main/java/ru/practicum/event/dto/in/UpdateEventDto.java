package ru.practicum.event.dto.in;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.practicum.event.dto.LocationDto;

@Data
@ToString
@Builder
public class UpdateEventDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;
}
