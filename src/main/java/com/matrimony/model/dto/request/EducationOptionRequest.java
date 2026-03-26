package com.matrimony.model.dto.request;

public class EducationOptionRequest {

    private String optionName;
    private Long categoryId;

    // Getters and Setters
    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
