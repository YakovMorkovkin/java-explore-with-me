package ru.practicum.comment.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByEventIdAndState(Long eventId, String state, Pageable pageable);

    List<Comment> findByAuthor_IdAndEvent_Id(Long userId, Long eventId, Pageable pageable);
}
