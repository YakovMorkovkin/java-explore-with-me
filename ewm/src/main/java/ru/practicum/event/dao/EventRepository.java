package ru.practicum.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long initiatorId);

    List<Event> findAllByCategoryId(Long categoryId);

    Event findByInitiatorIdAndId(Long initiatorId, Long eventId);

    @Query(value = "SELECT * " +
            "FROM events " +
            "WHERE (initiator_id IN :users OR :users IS NULL) " +
            "AND (category_id IN :categories  OR :categories IS NULL) " +
            "AND (event_date >= to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss')  OR to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss') IS NULL) " +
            "AND (event_date <= to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss')   OR to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss') IS NULL) " +
            "OFFSET :from " +
            "LIMIT :size", nativeQuery = true)
    List<Event> searchWithoutStateByAdmin(@Param("users") List<Long> users,
                                          @Param("categories") List<Long> categories,
                                          @Param("rangeStart") LocalDateTime rangeStart,
                                          @Param("rangeEnd") LocalDateTime rangeEnd,
                                          @Param("from") Long from,
                                          @Param("size") Long size);

    @Query(value = "SELECT * " +
            "FROM events " +
            "WHERE (initiator_id IN :users OR :users IS NULL) " +
            "AND state IN :states " +
            "AND (category_id IN :categories  OR :categories IS NULL) " +
            "AND (event_date >= to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss')  OR to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss') IS NULL) " +
            "AND (event_date <= to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss')   OR to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss') IS NULL) " +
            "OFFSET :from " +
            "LIMIT :size", nativeQuery = true)
    List<Event> searchByAdmin(@Param("users") List<Long> users,
                              @Param("states") List<String> states,
                              @Param("categories") List<Long> categories,
                              @Param("rangeStart") LocalDateTime rangeStart,
                              @Param("rangeEnd") LocalDateTime rangeEnd,
                              @Param("from") Long from,
                              @Param("size") Long size);

    @Query(value = "SELECT * " +
            "FROM events  " +
            "WHERE (lower(annotation) LIKE '%'||lower(:text)||'%' OR lower(description) LIKE '%'||lower(:text)||'%') " +
            "AND (category_id IN :categories  OR :categories IS NULL) " +
            "AND (:paid IS NULL OR paid = :paid) " +
            "AND (event_date BETWEEN " +
            "to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss') AND to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss') " +
            "OR event_date > CURRENT_TIMESTAMP) " +
            "AND (participant_limit < confirmed_requests) = :onlyAvailable " +
            "ORDER BY lower(:sort) " +
            "OFFSET :from " +
            "LIMIT :size", nativeQuery = true)
    List<Event> searchPublic(@Param("text") String text,
                             @Param("categories") List<Long> categories,
                             @Param("paid") Boolean paid,
                             @Param("rangeStart") String rangeStart,
                             @Param("rangeEnd") String rangeEnd,
                             @Param("onlyAvailable") Boolean onlyAvailable,
                             @Param("sort") String sort,
                             @Param("from") Long from,
                             @Param("size") Long size);

}
