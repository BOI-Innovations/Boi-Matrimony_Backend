package com.matrimony.model.dto.response;

import java.util.List;

public class EducationCategoryResponse {

    private Long id;
    private String categoryName;
    private List<EducationOptionResponse> educationOptions;

    // Getters and Setters
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

    public List<EducationOptionResponse> getEducationOptions() {
        return educationOptions;
    }

    public void setEducationOptions(List<EducationOptionResponse> educationOptions) {
        this.educationOptions = educationOptions;
    }
}
