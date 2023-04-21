package ru.practicum.category.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public interface CategoryService {
    CategoryDto addCategory(@Valid NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, @Valid NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategorys(Long from, Long size);

    CategoryDto getCategory(Long catId);
}
