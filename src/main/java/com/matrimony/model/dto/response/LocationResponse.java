package com.matrimony.model.dto.response;

import com.matrimony.model.enums.ResidencyStatus;

public class LocationResponse {

	private Long id;
	private String city;
	private String state;
	private String country;
	private String postalCode;
	private String citizenship;
	private ResidencyStatus residencyStatus;
	private Integer livingSinceYear;

	public Integer getLivingSinceYear() {
		return livingSinceYear;
	}

	public void setLivingSinceYear(Integer livingSinceYear) {
		this.livingSinceYear = livingSinceYear;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
