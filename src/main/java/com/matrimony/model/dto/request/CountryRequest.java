package com.matrimony.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CountryRequest {

	@NotBlank(message = "Country name is required")
	private String countryName;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

}
