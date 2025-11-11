package com.nelon.blog.services.interfaces;

import com.nelon.blog.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
	List<Category> getAllCategories();
	Category createCategory(Category category);
	void deleteCategory(UUID id);
}
