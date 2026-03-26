package com.matrimony.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class EducationCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String categoryName;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<EducationOption> educationOptions = new ArrayList<>();

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

	public List<EducationOption> getEducationOptions() {
		return educationOptions;
	}

	public void setEducationOptions(List<EducationOption> educationOptions) {
		this.educationOptions = educationOptions;
	}
}
