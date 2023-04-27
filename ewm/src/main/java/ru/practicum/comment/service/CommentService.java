package ru.practicum.comment.service;

import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;

import javax.validation.Valid;
import java.util.List;

@Service
public interface CommentService {
    CommentDto addComment(Long userId, @Valid NewCommentDto comment);

    void deleteComment(Long userId, Long commentId);

    List<CommentDto> publishComments(List<Long> commentsIds);

    List<CommentDto> getComments(Long eventId, int page, int limit);

    List<CommentDto> getUserComments(Long userId, Long eventId, int page, int limit);

    CommentDto updComment(Long userId, Long commentId, NewCommentDto newCommentDto);

    CommentDto getComment(Long commentId);
}
