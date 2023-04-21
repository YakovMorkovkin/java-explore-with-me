package ru.practicum.event.dto.in;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class NewRequestsStatusDto {
    private List<Long> requestIds;
    private String status;
}
