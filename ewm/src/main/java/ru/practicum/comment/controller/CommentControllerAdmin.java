package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentControllerAdmin {

    private final CommentService commentService;

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> editEvent(@RequestParam List<Long> ids) {
        return commentService.publishComments(ids);
    }
}
