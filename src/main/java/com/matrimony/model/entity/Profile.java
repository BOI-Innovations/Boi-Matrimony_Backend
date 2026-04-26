package com.matrimony.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrimony.model.enums.DietaryHabits;
import com.matrimony.model.enums.DrinkingHabits;
import com.matrimony.model.enums.Gender;
import com.matrimony.model.enums.Manglik;
import com.matrimony.model.enums.MaritalStatus;
import com.matrimony.model.enums.PhysicalStatus;
import com.matrimony.model.enums.ProfileVerificationStatus;
import com.matrimony.model.enums.Religion;
import com.matrimony.model.enums.SmokingHabits;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "profiles")
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", unique = true, nullable = false)
	@JsonBackReference
	private User user;

	@Column(nullable = false, length = 50)
	private String firstName;

	@Column(nullable = false, length = 50)
	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;

	@Column(nullable = false)
	private String profileCreatedFor;

	@Column(nullable = false)
	private LocalDate dateOfBirth;

	private LocalTime timeOfBirth;
	private String placeOfBirth;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Religion religion;

	@Column(nullable = false)
	private String caste;

	@Column(nullable = false)
	private String subCaste;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MaritalStatus maritalStatus;

	private String heightIn;

	private String weight;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PhysicalStatus physicalStatus;

	@Column(name = "has_disease", nullable = false)
	private Boolean hasDisease = false;

	@Column(name = "disease_details", length = 1000)
	private String diseaseDetails;

	private String motherTongue;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_languages", joinColumns = @JoinColumn(name = "profile_id"))
	@Column(name = "language")
	private Set<String> languagesKnown = new HashSet<>();

	@Column(nullable = false)
	private String gothra;

	private String star;

	private String rashi;

	@Enumerated(EnumType.STRING)
	private Manglik manglik;

	@Column(length = 1000)
	private String about;

	private String profilePictureUrl;

	@Enumerated(EnumType.STRING)
	private DietaryHabits dietaryHabits;

	@Enumerated(EnumType.STRING)
	private DrinkingHabits drinkingHabits;

	@Enumerated(EnumType.STRING)
	private SmokingHabits smokingHabits;

	@Enumerated(EnumType.STRING)
	private ProfileVerificationStatus verificationStatus = ProfileVerificationStatus.PENDING;

	@NotNull(message = "User must accept the declaration")
	@Column(name = "declaration_accepted")
	private Boolean declarationAccepted = false;

	@Column(name = "declaration_text", length = 2000)
	private String declarationText;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private Boolean profileComplete = false;
	private Integer profileCompletionPercentage = 0;

	// --- LAZY Relations ---

	@OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private Location location;

	@OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private EducationOccupationDetails educationOccupationDetails;

	@OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private HobbiesAndInterests hobbiesAndInterests;

	@OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private ProfilePreference preference;

	@OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<GalleryImage> images = new ArrayList<>();

	@OneToOne(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private FamilyDetails familyDetails;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public void addProfileImage(GalleryImage image) {
		if (image != null) {
			images.add(image);
			image.setProfile(this);
		}
	}

	public void removeProfileImage(GalleryImage image) {
		if (image != null && images.contains(image)) {
			images.remove(image);
			image.setProfile(null);
		}
	}

	public void setFamilyDetails(FamilyDetails details) {
		this.familyDetails = details;
		if (details != null) {
			details.setProfile(this);
		}
	}

	public void setPreference(ProfilePreference preference) {
		this.preference = preference;
		if (preference != null) {
			preference.setProfile(this);
		}
	}

	public void setLocation(Location location) {
		this.location = location;
		if (location != null) {
			location.setProfile(this);
		}
	}

	public void setEducationOccupationDetails(EducationOccupationDetails details) {
		this.educationOccupationDetails = details;
		if (details != null) {
			details.setProfile(this);
		}
	}

	public void setHobbiesAndInterests(HobbiesAndInterests hobbiesAndInterests) {
		this.hobbiesAndInterests = hobbiesAndInterests;
		if (hobbiesAndInterests != null) {
			hobbiesAndInterests.setProfile(this);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public void setImages(List<GalleryImage> images) {
		this.images = images;
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

	public Location getLocation() {
		return location;
	}

	public EducationOccupationDetails getEducationOccupationDetails() {
		return educationOccupationDetails;
	}

	public HobbiesAndInterests getHobbiesAndInterests() {
		return hobbiesAndInterests;
	}

	public ProfilePreference getPreference() {
		return preference;
	}

	public List<GalleryImage> getImages() {
		return images;
	}

	public FamilyDetails getFamilyDetails() {
		return familyDetails;
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
