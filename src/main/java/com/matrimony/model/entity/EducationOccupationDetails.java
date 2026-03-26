package com.matrimony.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrimony.model.enums.EmploymentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "education_occupation_details")
public class EducationOccupationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id", nullable = false)
	@JsonIgnore
	@NotNull(message = "Profile is required")
	private Profile profile;

	@NotBlank(message = "Highest education is required")
	@Size(max = 100)
	@Column(name = "highest_education", nullable = false)
	private String highestEducation;

	private String additionalDegree;
	private String collegeInstitution;

	@Column(length = 1000)
	private String educationInDetail;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Employment type is required")
	@Column(name = "employed_in", nullable = false)
	private EmploymentType employedIn;

	@NotBlank(message = "Occupation is required")
	@Column(nullable = false)
	private String occupation;

	@Size(max = 200)
	private String occupationInDetail;

	@NotBlank(message = "Organization name is required")
	@Size(max = 200)
	@Column(name = "organization_name", nullable = false)
	private String organizationName;

	private String annualIncome;
	private String incomeCurrency;

	@NotBlank(message = "Work city is required")
	@Column(name = "work_city", nullable = false)
	private String workCity;

	@NotBlank(message = "Work country is required")
	@Column(name = "work_country", nullable = false)
	private String workCountry;

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

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

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
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
}