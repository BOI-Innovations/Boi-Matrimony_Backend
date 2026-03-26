package com.matrimony.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrimony.model.enums.ResidencyStatus;

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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "location")
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id", nullable = false)
	@JsonIgnore
	@NotNull(message = "Profile is required")
	private Profile profile;

	@NotNull(message = "City is required")
	private String city;

	@NotNull(message = "State is required")
	private String state;

	@NotNull(message = "Country is required")
	private String country;

	private String postalCode;

	@NotNull(message = "Citizenship is required")
	private String citizenship;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Residency status is required")
	private ResidencyStatus residencyStatus = ResidencyStatus.NOT_DISCLOSED;

	private Integer livingSinceYear;

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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public ResidencyStatus getResidencyStatus() {
		return residencyStatus;
	}

	public void setResidencyStatus(ResidencyStatus residencyStatus) {
		this.residencyStatus = residencyStatus;
	}

	public Integer getLivingSinceYear() {
		return livingSinceYear;
	}

	public void setLivingSinceYear(Integer livingSinceYear) {
		this.livingSinceYear = livingSinceYear;
	}
}