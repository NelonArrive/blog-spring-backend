package com.nelon.blog.services.interfaces;

import com.nelon.blog.domain.entities.Category;

import java.util.List;

public interface ICategoryService {
	List<Category> getAllCategories();
}
