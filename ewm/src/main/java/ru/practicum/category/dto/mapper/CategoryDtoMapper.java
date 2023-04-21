package ru.practicum.category.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {
    Category toModel(NewCategoryDto newCategoryDto);

    CategoryDto toDto(Category category);
}
