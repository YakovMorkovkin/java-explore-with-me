package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dao.CommentRepository;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.mapper.CommentDtoMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.enums.ForbiddenWords;
import ru.practicum.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.notfound.CommentNotFoundException;
import ru.practicum.exception.unavailable.UnavailableException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentDtoMapper commentDtoMapper;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public CommentDto addComment(Long userId, NewCommentDto commentDto) {
        User author = userService.getUserCheked(userId);
        Event event = eventService.getEventChecked(commentDto.getEventId());
        if (event.getState().equals(State.PUBLISHED.name())) {
            Comment comment = Comment.builder()
                    .text(commentDto.getText())
                    .author(author)
                    .event(event)
                    .state(State.PENDING.name())
                    .created(LocalDateTime.now())
                    .build();
            return commentDtoMapper.toDto(commentRepository.save(comment));
        } else {
            throw new UnavailableException("Only published events allow to comment");
        }
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        User author = userService.getUserCheked(userId);
        Comment comment = getCheckedComment(commentId);
        if (comment.getAuthor().equals(author)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new UnavailableException("Only owner can delete comment");
        }
    }

    @Override
    public List<CommentDto> publishComments(List<Long> commentsIds) {
        List<Comment> allComments = commentRepository.findAllById(commentsIds);
        List<Comment> publishedComments = allComments.stream()
                .filter(x -> !containsForbiddenWords(x.getText()))
                .peek(x -> x.setState(State.PUBLISHED.name()))
                .collect(Collectors.toList());
        List<Comment> rejectedComments = allComments.stream()
                .filter(x -> !publishedComments.contains(x))
                .collect(Collectors.toList());
        commentRepository.deleteAll(rejectedComments);
        return commentRepository.saveAll(publishedComments).stream()
                .map(commentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getComments(Long eventId, int page, int limit) {
        getCheckedComment(eventId);
        List<Comment> comments = commentRepository.findByEventIdAndState(eventId, State.PUBLISHED.name(),
                PageRequest.of(page, limit, Sort.by("id").descending()));
        return comments.stream()
                .map(commentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    private Comment getCheckedComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new CommentNotFoundException(id)
        );
    }

    private boolean containsForbiddenWords(String text) {
        for (ForbiddenWords fw : ForbiddenWords.values()) {
            if (text.toLowerCase().contains(fw.name().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
