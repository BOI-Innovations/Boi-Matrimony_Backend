package com.matrimony.model.dto.response;

import java.util.Set;

import com.matrimony.model.enums.DietaryHabits;
import com.matrimony.model.enums.DrinkingHabits;
import com.matrimony.model.enums.EducationType;
import com.matrimony.model.enums.Gender;
import com.matrimony.model.enums.HaveChildren;
import com.matrimony.model.enums.Manglik;
import com.matrimony.model.enums.MaritalStatus;
import com.matrimony.model.enums.PhysicalStatus;
import com.matrimony.model.enums.Religion;
import com.matrimony.model.enums.SmokingHabits;

public class ProfilePreferenceResponse {

	private Long id;
	private Integer minAge;
	private Integer maxAge;
	private String minHeight;
	private String maxHeight;
	private Gender gender;
	private MaritalStatus maritalStatus;
	private HaveChildren haveChildren;
	private PhysicalStatus physicalStatus;

	private Set<String> motherTongues;
	private Religion religion;
	private Set<String> castes;
	private Set<String> subCastes;
	private Set<String> gothras;
	private Set<String> stars;
	private Set<String> rashis;
	private Manglik manglik;
	private EducationType educationType;
	private Set<String> educationLevels;
	private Set<String> employedIn;
	private Set<String> occupations;
	private String annualIncome;
	private DietaryHabits dietaryHabits;
	private SmokingHabits smokingHabits;
	private DrinkingHabits drinkingHabits;
	private Set<String> citizenships;
	private Set<String> countriesLivedIn;
	private String aboutMyPartner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public HaveChildren getHaveChildren() {
		return haveChildren;
	}

	public void setHaveChildren(HaveChildren haveChildren) {
		this.haveChildren = haveChildren;
	}

	public PhysicalStatus getPhysicalStatus() {
		return physicalStatus;
	}

	public void setPhysicalStatus(PhysicalStatus physicalStatus) {
		this.physicalStatus = physicalStatus;
	}

	public Set<String> getMotherTongues() {
		return motherTongues;
	}

	public void setMotherTongues(Set<String> motherTongues) {
		this.motherTongues = motherTongues;
	}

	public Religion getReligion() {
		return religion;
	}

	public void setReligion(Religion religion) {
		this.religion = religion;
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

	public Manglik getManglik() {
		return manglik;
	}

	public void setManglik(Manglik manglik) {
		this.manglik = manglik;
	}

	public EducationType getEducationType() {
		return educationType;
	}

	public void setEducationType(EducationType educationType) {
		this.educationType = educationType;
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

	public DietaryHabits getDietaryHabits() {
		return dietaryHabits;
	}

	public void setDietaryHabits(DietaryHabits dietaryHabits) {
		this.dietaryHabits = dietaryHabits;
	}

	public SmokingHabits getSmokingHabits() {
		return smokingHabits;
	}

	public void setSmokingHabits(SmokingHabits smokingHabits) {
		this.smokingHabits = smokingHabits;
	}

	public DrinkingHabits getDrinkingHabits() {
		return drinkingHabits;
	}

	public void setDrinkingHabits(DrinkingHabits drinkingHabits) {
		this.drinkingHabits = drinkingHabits;
	}

	public Set<String> getCitizenships() {
		return citizenships;
	}

	public void setCitizenships(Set<String> citizenships) {
		this.citizenships = citizenships;
	}

	public Set<String> getCountriesLivedIn() {
		return countriesLivedIn;
	}

	public void setCountriesLivedIn(Set<String> countriesLivedIn) {
		this.countriesLivedIn = countriesLivedIn;
	}

	public String getAboutMyPartner() {
		return aboutMyPartner;
	}

	public void setAboutMyPartner(String aboutMyPartner) {
		this.aboutMyPartner = aboutMyPartner;
	}

}
