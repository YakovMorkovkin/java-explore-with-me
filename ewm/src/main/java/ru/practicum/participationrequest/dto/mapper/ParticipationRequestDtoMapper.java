package ru.practicum.participationrequest.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.participationrequest.model.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface ParticipationRequestDtoMapper {
    //ParticipationRequest toModel(ParticipationRequestDto participationRequestDto);

    @Mapping(target = "event", source = "participationRequest.event.id")
    @Mapping(target = "requester", source = "participationRequest.requester.id")
    ParticipationRequestDto toDto(ParticipationRequest participationRequest);
}
