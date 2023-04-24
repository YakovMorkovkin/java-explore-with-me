package ru.practicum.statsservice;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.statsdto.HitDtoIn;

import static ru.practicum.statsservice.Constants.DT_PATTERN;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HitMapper {
    @Mapping(source = "timestamp", target = "timestamp", dateFormat = DT_PATTERN)
    Hit toModel(HitDtoIn hitDtoIn);
}