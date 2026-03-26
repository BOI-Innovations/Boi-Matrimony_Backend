package com.matrimony.model.dto.request;

import java.util.Set;

public class ProfileFilterRequest {
	// Basic Preferences
	private String gender;
	private String maritalStatus;
	private Integer minAge;
	private Integer maxAge;
	private String minHeight;
	private String maxHeight;
	private String haveChildren;
	private String physicalStatus;

	// Religion & Community
	private String religion;
	private Set<String> motherTongues;
	private Set<String> castes;
	private Set<String> subCastes;
	private Set<String> gothras;

	// Horoscope Details
	private String manglik;
	private Set<String> stars;
	private Set<String> rashis;

	// Education & Profession
	private Set<String> educationLevels;
	private Set<String> employedIn;
	private Set<String> occupations;
	private String annualIncome;

	// Lifestyle Preferences
	private String dietaryHabits;
	private String smokingHabits;
	private String drinkingHabits;

	// Location Preferences
	private Set<String> countriesLivedIn;
	private Set<String> citizenships;

	// Pagination
	private Integer page = 0;
	private Integer size = 20;

	// Getters and Setters
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
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

	public String getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(String minHeight) {
		this.minHeight = minHeight;
	}

	public String getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(String maxHeight) {
		this.maxHeight = maxHeight;
	}

	public String getHaveChildren() {
		return haveChildren;
	}

	public void setHaveChildren(String haveChildren) {
		this.haveChildren = haveChildren;
	}

	public String getPhysicalStatus() {
		return physicalStatus;
	}

	public void setPhysicalStatus(String physicalStatus) {
		this.physicalStatus = physicalStatus;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public Set<String> getMotherTongues() {
		return motherTongues;
	}

	public void setMotherTongues(Set<String> motherTongues) {
		this.motherTongues = motherTongues;
	}

	public Set<String> getCastes() {
		return castes;
	}

	public void setCastes(Set<String> castes) {
		this.castes = castes;
	}

	public Set<String> getSubCastes() {
		return subCastes;
	}

	public void setSubCastes(Set<String> subCastes) {
		this.subCastes = subCastes;
	}

	public Set<String> getGothras() {
		return gothras;
	}

	public void setGothras(Set<String> gothras) {
		this.gothras = gothras;
	}

	public String getManglik() {
		return manglik;
	}

	public void setManglik(String manglik) {
		this.manglik = manglik;
	}

	public Set<String> getStars() {
		return stars;
	}

	public void setStars(Set<String> stars) {
		this.stars = stars;
	}

	public Set<String> getRashis() {
		return rashis;
	}

	public void setRashis(Set<String> rashis) {
		this.rashis = rashis;
	}

	public Set<String> getEducationLevels() {
		return educationLevels;
	}

	public void setEducationLevels(Set<String> educationLevels) {
		this.educationLevels = educationLevels;
	}

	public Set<String> getEmployedIn() {
		return employedIn;
	}

	public void setEmployedIn(Set<String> employedIn) {
		this.employedIn = employedIn;
	}

	public Set<String> getOccupations() {
		return occupations;
	}

	public void setOccupations(Set<String> occupations) {
		this.occupations = occupations;
	}

	public String getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getDietaryHabits() {
		return dietaryHabits;
	}

	public void setDietaryHabits(String dietaryHabits) {
		this.dietaryHabits = dietaryHabits;
	}

	public String getSmokingHabits() {
		return smokingHabits;
	}

	public void setSmokingHabits(String smokingHabits) {
		this.smokingHabits = smokingHabits;
	}

	public String getDrinkingHabits() {
		return drinkingHabits;
	}

	public void setDrinkingHabits(String drinkingHabits) {
		this.drinkingHabits = drinkingHabits;
	}

	public Set<String> getCountriesLivedIn() {
		return countriesLivedIn;
	}

	public void setCountriesLivedIn(Set<String> countriesLivedIn) {
		this.countriesLivedIn = countriesLivedIn;
	}

	public Set<String> getCitizenships() {
		return citizenships;
	}

	public void setCitizenships(Set<String> citizenships) {
		this.citizenships = citizenships;
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
}