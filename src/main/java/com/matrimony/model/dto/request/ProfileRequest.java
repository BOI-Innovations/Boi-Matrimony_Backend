package com.matrimony.model.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.matrimony.model.enums.DietaryHabits;
import com.matrimony.model.enums.DrinkingHabits;
import com.matrimony.model.enums.Gender;
import com.matrimony.model.enums.Manglik;
import com.matrimony.model.enums.MaritalStatus;
import com.matrimony.model.enums.PhysicalStatus;
import com.matrimony.model.enums.Religion;
import com.matrimony.model.enums.SmokingHabits;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class ProfileRequest {

	@NotBlank(message = "First name is required")
	@Size(max = 50, message = "First name must be at most 50 characters")
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Size(max = 50, message = "Last name must be at most 50 characters")
	private String lastName;

	@NotNull(message = "Gender is required")
	private Gender gender;

	@NotBlank(message = "Profile created by is required")
	@Size(max = 50, message = "Profile created by must be at most 50 characters")
	private String profileCreatedFor;

	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be in the past")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime timeOfBirth;

	@Size(max = 100, message = "Place of birth must be at most 100 characters")
	private String placeOfBirth;

	@NotNull(message = "Religion is required")
	private Religion religion;

	@NotBlank(message = "Caste is required")
	@Size(max = 50, message = "Caste must be at most 50 characters")
	private String caste;

	@NotBlank(message = "Sub-caste is required")
	@Size(max = 50, message = "Sub-caste must be at most 50 characters")
	private String subCaste;

	@NotNull(message = "Marital status is required")
	private MaritalStatus maritalStatus;

	private String heightIn;

	private String weight;

	@NotNull(message = "Physical status is required")
	private PhysicalStatus physicalStatus;

	@NotNull(message = "Disease information is required")
	private Boolean hasDisease;

	@Size(max = 1000, message = "Disease details must be at most 1000 characters")
	private String diseaseDetails;

	@Size(max = 50, message = "Mother tongue must be at most 50 characters")
	private String motherTongue;

	private Set<@Size(max = 50, message = "Each language must be at most 50 characters") String> languagesKnown;

	@NotBlank(message = "Gothra is required")
	@Size(max = 50, message = "Gothra must be at most 50 characters")
	private String gothra;

	@Size(max = 50, message = "rashi must be at most 50 characters")
	private String star;

	@Size(max = 50, message = "Rashi must be at most 50 characters")
	private String rashi;

	private Manglik manglik;

	@Size(max = 1000, message = "About section must be at most 1000 characters")
	private String about;

	@NotNull(message = "Dietary habits is required")
	private DietaryHabits dietaryHabits;

	@NotNull(message = "Drinking habits is required")
	private DrinkingHabits drinkingHabits;

	@NotNull(message = "Smoking habits is required")
	private SmokingHabits smokingHabits;

	@NotNull(message = "You must accept the declaration")
	private Boolean declarationAccepted;

	@Size(max = 2000)
	private String declarationText;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getProfileCreatedFor() {
		return profileCreatedFor;
	}

	public void setProfileCreatedFor(String profileCreatedFor) {
		this.profileCreatedFor = profileCreatedFor;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public LocalTime getTimeOfBirth() {
		return timeOfBirth;
	}

	public void setTimeOfBirth(LocalTime timeOfBirth) {
		this.timeOfBirth = timeOfBirth;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public Religion getReligion() {
		return religion;
	}

	public void setReligion(Religion religion) {
		this.religion = religion;
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

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getHeightIn() {
		return heightIn;
	}

	public void setHeightIn(String heightIn) {
		this.heightIn = heightIn;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public PhysicalStatus getPhysicalStatus() {
		return physicalStatus;
	}

	public void setPhysicalStatus(PhysicalStatus physicalStatus) {
		this.physicalStatus = physicalStatus;
	}

	public String getMotherTongue() {
		return motherTongue;
	}

	public void setMotherTongue(String motherTongue) {
		this.motherTongue = motherTongue;
	}

	public Set<String> getLanguagesKnown() {
		return languagesKnown;
	}

	public void setLanguagesKnown(Set<String> languagesKnown) {
		this.languagesKnown = languagesKnown;
	}

	public String getGothra() {
		return gothra;
	}

	public void setGothra(String gothra) {
		this.gothra = gothra;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getRashi() {
		return rashi;
	}

	public void setRashi(String rashi) {
		this.rashi = rashi;
	}

	public Manglik getManglik() {
		return manglik;
	}

	public void setManglik(Manglik manglik) {
		this.manglik = manglik;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public DietaryHabits getDietaryHabits() {
		return dietaryHabits;
	}

	public void setDietaryHabits(DietaryHabits dietaryHabits) {
		this.dietaryHabits = dietaryHabits;
	}

	public DrinkingHabits getDrinkingHabits() {
		return drinkingHabits;
	}

	public void setDrinkingHabits(DrinkingHabits drinkingHabits) {
		this.drinkingHabits = drinkingHabits;
	}

	public SmokingHabits getSmokingHabits() {
		return smokingHabits;
	}

	public void setSmokingHabits(SmokingHabits smokingHabits) {
		this.smokingHabits = smokingHabits;
	}

	public Boolean getHasDisease() {
		return hasDisease;
	}

	public void setHasDisease(Boolean hasDisease) {
		this.hasDisease = hasDisease;
	}

	public String getDiseaseDetails() {
		return diseaseDetails;
	}

	public void setDiseaseDetails(String diseaseDetails) {
		this.diseaseDetails = diseaseDetails;
	}

	public Boolean getDeclarationAccepted() {
		return declarationAccepted;
	}

	public void setDeclarationAccepted(Boolean declarationAccepted) {
		this.declarationAccepted = declarationAccepted;
	}

	public String getDeclarationText() {
		return declarationText;
	}

	public void setDeclarationText(String declarationText) {
		this.declarationText = declarationText;
	}

}
