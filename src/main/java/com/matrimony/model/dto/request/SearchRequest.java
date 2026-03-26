package com.matrimony.model.dto.request;

import com.matrimony.model.enums.Gender;
import com.matrimony.model.enums.MaritalStatus;
import com.matrimony.model.enums.Religion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class SearchRequest {

	private Gender gender;

	private Religion religion;

	@Min(value = 18, message = "Minimum age must be at least 18")
	private Integer minAge;

	@Min(value = 18, message = "Maximum age must be at least 18")
	private Integer maxAge;

	private MaritalStatus maritalStatus;

	@Size(max = 50, message = "Caste must be at most 50 characters")
	private String caste;

	@Size(max = 50, message = "Sub-caste must be at most 50 characters")
	private String subCaste;

	@Size(max = 100, message = "Education must be at most 100 characters")
	private String education;

	@Size(max = 100, message = "Occupation must be at most 100 characters")
	private String occupation;

	@Min(value = 0, message = "Minimum annual income cannot be negative")
	private Double minAnnualIncome;

	@Size(max = 50, message = "City must be at most 50 characters")
	private String city;

	@Size(max = 50, message = "Country must be at most 50 characters")
	private String country;

	private Integer page = 0;

	private Integer size = 10;

	private String sortBy = "createdAt";

	private String sortDirection = "DESC";

	// Getters and Setters
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Religion getReligion() {
		return religion;
	}

	public void setReligion(Religion religion) {
		this.religion = religion;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getCaste() {
		return caste;
	}

	public void setCaste(String caste) {
		this.caste = caste;
	}

	public String getSubCaste() {
		return subCaste;
	}

	public void setSubCaste(String subCaste) {
		this.subCaste = subCaste;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Double getMinAnnualIncome() {
		return minAnnualIncome;
	}

	public void setMinAnnualIncome(Double minAnnualIncome) {
		this.minAnnualIncome = minAnnualIncome;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
}