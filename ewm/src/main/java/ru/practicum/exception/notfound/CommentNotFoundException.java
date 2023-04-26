package ru.practicum.exception.notfound;

public class CommentNotFoundException  extends NotFoundException {
    public CommentNotFoundException(Long commentId) {
        super(String.format("Comment with id - %d not found", commentId));
    }
}
