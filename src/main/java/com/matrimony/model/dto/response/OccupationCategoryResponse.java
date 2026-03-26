package com.matrimony.model.dto.response;

import java.util.List;

public class OccupationCategoryResponse {
	private Long id;
	private String categoryName;
	private List<OccupationOptionResponse> occupationOptions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<OccupationOptionResponse> getOccupationOptions() {
		return occupationOptions;
	}

	public void setOccupationOptions(List<OccupationOptionResponse> occupationOptions) {
		this.occupationOptions = occupationOptions;
	}

}
