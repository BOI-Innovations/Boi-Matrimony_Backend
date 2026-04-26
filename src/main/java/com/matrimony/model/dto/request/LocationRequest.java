package com.matrimony.model.dto.request;

import com.matrimony.model.enums.ResidencyStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LocationRequest {

	private String city;

	private String state;

	@NotBlank(message = "Country is required")
	private String country;

	private String postalCode;

	private String citizenship;

	@NotNull(message = "Residency status is required")
	private ResidencyStatus residencyStatus;

	private Integer livingSinceYear;

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