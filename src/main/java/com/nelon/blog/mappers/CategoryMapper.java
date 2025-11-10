package com.nelon.blog.mappers;

import com.nelon.blog.domain.dto.CategoryDto;
import com.nelon.blog.domain.dto.CreateCategoryRequest;
import com.nelon.blog.domain.entities.Category;
import com.nelon.blog.domain.entities.Post;
import com.nelon.blog.domain.entities.PostStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
	@Mapping(
		target = "postCount",
		source = "posts",
		qualifiedByName = "calculatePostCount"
	)
	CategoryDto toDto(Category category);
	
	Category toEntity(CreateCategoryRequest createcategoryRequest);
	
	@Named("calculatePostCount")
	default long calculatePostCount(List<Post> posts) {
		if (null == posts) {
			return 0;
		}
		return posts.stream()
			.filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
			.count();
	}
}
