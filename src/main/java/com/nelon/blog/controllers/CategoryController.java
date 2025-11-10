package com.nelon.blog.controllers;

import com.nelon.blog.domain.dto.CategoryDto;
import com.nelon.blog.mappers.CategoryMapper;
import com.nelon.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<CategoryDto> createCategory() {
	
	}
}
