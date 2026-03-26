package com.matrimony.model.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profile_preferences")
public class ProfilePreference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id", unique = true, nullable = false)
	@JsonIgnore
	private Profile profile;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;

	@Column(nullable = false)
	private Integer minAge;

	@Column(nullable = false)
	private Integer maxAge;

	private String minHeight;
	private String maxHeight;

	@Enumerated(EnumType.STRING)
	private MaritalStatus maritalStatus;

	@Enumerated(EnumType.STRING)
	private HaveChildren haveChildren;

	@Enumerated(EnumType.STRING)
	private PhysicalStatus physicalStatus;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_mother_tongues", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "mother_tongue")
	private Set<String> motherTongues = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Religion religion;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_castes", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "caste", nullable = false)
	private Set<String> castes = new HashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_subcastes", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "sub_caste", nullable = false)
	private Set<String> subCastes = new HashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_gothras", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "gothra", nullable = false)
	private Set<String> gothras = new HashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_stars", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "star")
	private Set<String> stars = new HashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_rashis", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "rashi")
	private Set<String> rashis = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column(name = "manglik", nullable = false)
	private Manglik manglik;

	@Enumerated(EnumType.STRING)
	private EducationType educationType;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_education", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "education_level")
	private Set<String> educationLevels = new HashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_employed_in", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "employed_in")
	private Set<String> employedIn = new HashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_occupations", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "occupation")
	private Set<String> occupations = new HashSet<>();

	private String annualIncome;

	@Enumerated(EnumType.STRING)
	private DietaryHabits dietaryHabits;

	@Enumerated(EnumType.STRING)
	private SmokingHabits smokingHabits;

	@Enumerated(EnumType.STRING)
	private DrinkingHabits drinkingHabits;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_citizenship", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "citizenship")
	private Set<String> citizenships = new HashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_preference_country_lived_in", joinColumns = @JoinColumn(name = "profile_pref_id"))
	@Column(name = "country_lived_in")
	private Set<String> countriesLivedIn = new HashSet<>();

	@Column(length = 2000)
	private String aboutMyPartner;

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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
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
