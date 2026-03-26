package com.matrimony.model.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.matrimony.model.enums.DietaryHabits;
import com.matrimony.model.enums.DrinkingHabits;
import com.matrimony.model.enums.Gender;
import com.matrimony.model.enums.Manglik;
import com.matrimony.model.enums.MaritalStatus;
import com.matrimony.model.enums.PhysicalStatus;
import com.matrimony.model.enums.ProfileVerificationStatus;
import com.matrimony.model.enums.Religion;
import com.matrimony.model.enums.SmokingHabits;

public class ProfileResponse {

	private Long id;
	private Long userId;
	private String username;
	private String email;

	private String firstName;
	private String lastName;
	private Gender gender;

	private String profileCreatedFor;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	private Integer age;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime timeOfBirth;

	private String placeOfBirth;

	private Religion religion;
	private String caste;
	private String subCaste;
	private MaritalStatus maritalStatus;

	private String heightIn;
	private String weight;

	private PhysicalStatus physicalStatus;
	private Boolean hasDisease;

	private String diseaseDetails;

	private String motherTongue;
	private Set<String> languagesKnown;

	private String gothra;
	private String star;
	private String rashi;
	private Manglik manglik;

	private String about;
	private String profilePictureUrl;

	private Boolean declarationAccepted;
	private String declarationText;

	private DietaryHabits dietaryHabits;
	private DrinkingHabits drinkingHabits;
	private SmokingHabits smokingHabits;

	private ProfileVerificationStatus verificationStatus;
	private Boolean profileComplete;
	private Integer profileCompletionPercentage;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	private ProfilePreferenceResponse preference;
	private List<GalleryImageResponse> images;
	private FamilyDetailsResponse familyDetails;
	private HobbiesAndInterestsResponse hobbiesAndInterestsResponse;
	private EducationOccupationDetailsResponse educationOccupationDetailsResponse;
	private LocationResponse locationResponse;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
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

	public ProfileVerificationStatus getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(ProfileVerificationStatus verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public Boolean getProfileComplete() {
		return profileComplete;
	}

	public void setProfileComplete(Boolean profileComplete) {
		this.profileComplete = profileComplete;
	}

	public Integer getProfileCompletionPercentage() {
		return profileCompletionPercentage;
	}

	public void setProfileCompletionPercentage(Integer profileCompletionPercentage) {
		this.profileCompletionPercentage = profileCompletionPercentage;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public ProfilePreferenceResponse getPreference() {
		return preference;
	}

	public void setPreference(ProfilePreferenceResponse preference) {
		this.preference = preference;
	}

	public List<GalleryImageResponse> getImages() {
		return images;
	}

	public void setImages(List<GalleryImageResponse> images) {
		this.images = images;
	}

	public FamilyDetailsResponse getFamilyDetails() {
		return familyDetails;
	}

	public void setFamilyDetails(FamilyDetailsResponse familyDetails) {
		this.familyDetails = familyDetails;
	}

	public HobbiesAndInterestsResponse getHobbiesAndInterestsResponse() {
		return hobbiesAndInterestsResponse;
	}

	public void setHobbiesAndInterestsResponse(HobbiesAndInterestsResponse hobbiesAndInterestsResponse) {
		this.hobbiesAndInterestsResponse = hobbiesAndInterestsResponse;
	}

	public EducationOccupationDetailsResponse getEducationOccupationDetailsResponse() {
		return educationOccupationDetailsResponse;
	}

	public void setEducationOccupationDetailsResponse(
			EducationOccupationDetailsResponse educationOccupationDetailsResponse) {
		this.educationOccupationDetailsResponse = educationOccupationDetailsResponse;
	}

	public LocationResponse getLocationResponse() {
		return locationResponse;
	}

	public void setLocationResponse(LocationResponse locationResponse) {
		this.locationResponse = locationResponse;
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
