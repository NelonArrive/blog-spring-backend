package com.nelon.blog.controllers;

import com.nelon.blog.domain.dto.CategoryDto;
import com.nelon.blog.domain.dto.CreateCategoryRequest;
import com.nelon.blog.domain.entities.Category;
import com.nelon.blog.mappers.CategoryMapper;
import com.nelon.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/categories")
public class CategoryController {
	
	private final CategoryService categoryService;
	private final CategoryMapper categoryMapper;
	
	@GetMapping
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		List<CategoryDto> categories =
			categoryService.getAllCategories().stream()
				.map(categoryMapper::toDto)
				.toList();
		return ResponseEntity.ok(categories);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(
		@Valid @RequestBody CreateCategoryRequest createCategoryRequest
	) {
		Category categoryToCreate = categoryMapper.toEntity(createCategoryRequest);
		Category savedCategory = categoryService.createCategory(categoryToCreate);
		return new ResponseEntity<>(
			categoryMapper.toDto(savedCategory),
			HttpStatus.CREATED
		);
	}
}
