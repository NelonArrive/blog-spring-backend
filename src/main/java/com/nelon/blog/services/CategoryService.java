package com.nelon.blog.services;

import com.nelon.blog.domain.entities.Category;
import com.nelon.blog.repositories.CategoryRepository;
import com.nelon.blog.services.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
	
	private final CategoryRepository categoryRepository;
	
	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAllWithPostCount();
	}
}
