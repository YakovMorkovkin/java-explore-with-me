package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.category.dao.CategoryRepository;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.mapper.CategoryDtoMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.exception.ExistRelatedDataException;
import ru.practicum.exception.duplicate.CategoryNameDuplicateException;
import ru.practicum.exception.duplicate.EmailDuplicateException;
import ru.practicum.exception.notfound.CategoryNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryDtoMapper categoryDtoMapper;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        log.info("Add category {}", newCategoryDto.getName());
        try {
            return categoryDtoMapper.toDto(categoryRepository.save(categoryDtoMapper.toModel(newCategoryDto)));
        } catch (DataIntegrityViolationException e) {
            throw new CategoryNameDuplicateException(newCategoryDto.getName());
        }
    }

    @Override
    public void deleteCategory(Long catId) {
        log.info("Delete category with id - {}", catId);
        getCategoryChecked(catId);
        if (eventRepository.findAllByCategoryId(catId).isEmpty()) {
            categoryRepository.deleteById(catId);
        } else {
            throw new ExistRelatedDataException("Exists events related with category");
        }
    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        log.info("Update category with id - {}", catId);
        try {
            Category category = getCategoryChecked(catId);
            category.setName(newCategoryDto.getName());
            return categoryDtoMapper.toDto(categoryRepository.save(category));
        } catch (ConstraintViolationException e) {
            throw new EmailDuplicateException(newCategoryDto.getName());
        }
    }

    @Override
    public List<CategoryDto> getCategorys(Long from, Long size) {
        log.info("Get categories");
        return categoryRepository.findWithParams(from, size).stream()
                .map(categoryDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        log.info("Get category with id - {}", catId);
        return categoryDtoMapper.toDto(getCategoryChecked(catId));
    }

    private Category getCategoryChecked(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new CategoryNotFoundException(catId)
        );
    }


}
