package com.matrimony.model.dto.request;

import com.matrimony.model.enums.EmploymentType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EducationOccupationDetailsRequest {

	@Size(max = 100)
	private String highestEducation;

	private String additionalDegree;
	private String collegeInstitution;

	@Size(max = 1000)
	private String educationInDetail;

	@NotNull(message = "Employment type is required")
	private EmploymentType employedIn;

	private String occupation;

	@Size(max = 200)
	private String occupationInDetail;

	private String annualIncome;
	private String incomeCurrency;

	private String workCity;

	private String workCountry;

	@Size(max = 200)
	private String organizationName;

	// Getters and Setters

	public String getHighestEducation() {
		return highestEducation;
	}

	public void setHighestEducation(String highestEducation) {
		this.highestEducation = highestEducation;
	}

	public String getAdditionalDegree() {
		return additionalDegree;
	}

	public void setAdditionalDegree(String additionalDegree) {
		this.additionalDegree = additionalDegree;
	}

	public String getCollegeInstitution() {
		return collegeInstitution;
	}

	public void setCollegeInstitution(String collegeInstitution) {
		this.collegeInstitution = collegeInstitution;
	}

	public String getEducationInDetail() {
		return educationInDetail;
	}

	public void setEducationInDetail(String educationInDetail) {
		this.educationInDetail = educationInDetail;
	}

	public EmploymentType getEmployedIn() {
		return employedIn;
	}

	public void setEmployedIn(EmploymentType employedIn) {
		this.employedIn = employedIn;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getOccupationInDetail() {
		return occupationInDetail;
	}

	public void setOccupationInDetail(String occupationInDetail) {
		this.occupationInDetail = occupationInDetail;
	}

	public String getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getIncomeCurrency() {
		return incomeCurrency;
	}

	public void setIncomeCurrency(String incomeCurrency) {
		this.incomeCurrency = incomeCurrency;
	}

	public String getWorkCity() {
		return workCity;
	}

	public void setWorkCity(String workCity) {
		this.workCity = workCity;
	}

	public String getWorkCountry() {
		return workCountry;
	}

	public void setWorkCountry(String workCountry) {
		this.workCountry = workCountry;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
}