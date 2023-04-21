package ru.practicum.category.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.category.model.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * " +
            "FROM categories " +
            "ORDER BY id " +
            "OFFSET :from " +
            "LIMIT :size", nativeQuery = true)
    List<Category> findWithParams(Long from, Long size);
}
