package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryControllerPublic {
    private final CategoryService categoryService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCats(@RequestParam(value = "from", defaultValue = "0",
            required = false) Long offset,
                                     @RequestParam(value = "size", defaultValue = "10",
                                             required = false) Long limit) {
        return categoryService.getCategorys(offset, limit);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCat(@PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }
}
