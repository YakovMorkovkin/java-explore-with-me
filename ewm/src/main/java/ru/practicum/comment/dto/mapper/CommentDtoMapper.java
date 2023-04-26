package ru.practicum.comment.dto.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dto.mapper.EventDtoMapper;
import ru.practicum.user.dto.mapper.UserDtoMapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserDtoMapper.class, EventDtoMapper.class})
public interface CommentDtoMapper {
    @Mapping(target = "event.id", source = "eventId")
    Comment toModel(NewCommentDto commentDto);

    @Mapping(target = "eventId", source = "event.id")
    CommentDto toDto(Comment comment);

}
