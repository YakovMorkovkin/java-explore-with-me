package ru.practicum.event.dto.outside;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.Constants.DT_PATTERN;

@Data
@Builder
public class EventFullDto {
    private Long id;
    private UserShortDto initiator;
    @Length(min = 20, max = 2000)
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @Length(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = DT_PATTERN)
    private LocalDateTime eventDate;
    @JsonFormat(pattern = DT_PATTERN)
    private LocalDateTime createdOn;
    @JsonFormat(pattern = DT_PATTERN)
    private LocalDateTime publishedOn;
    private LocationDto location;
    private boolean paid;
    private Long participantLimit;
    private boolean requestModeration;
    private String title;
    private String state;
    private Long views;
}
