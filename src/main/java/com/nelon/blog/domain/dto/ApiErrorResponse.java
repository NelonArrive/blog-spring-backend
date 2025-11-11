package com.nelon.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
	private int status;
	private String message;
	private List<FieldErrors> errors;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FieldErrors {
		private String field;
		private String message;
	}
}
