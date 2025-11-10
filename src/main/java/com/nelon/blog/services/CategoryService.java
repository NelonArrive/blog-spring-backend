package com.nelon.blog.services;

import com.nelon.blog.domain.entities.Category;
import com.nelon.blog.repositories.CategoryRepository;
import com.nelon.blog.services.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
	
	private final CategoryRepository categoryRepository;
	
	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAllWithPostCount();
	}
	
	@Override
	@Transactional
	public Category createCategory(Category category) {
		if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
			throw new IllegalArgumentException("Category with name " + category.getName() + " already " +
				"exists");
		}
		return categoryRepository.save(category);
	}
}
