package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentControllerPrivate {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Long userId, @RequestBody NewCommentDto newCommentDto) {
        return commentService.addComment(userId, newCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
    }
}
