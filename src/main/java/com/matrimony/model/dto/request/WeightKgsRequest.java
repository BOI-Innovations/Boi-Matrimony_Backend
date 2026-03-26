package com.matrimony.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class WeightKgsRequest {

	@NotBlank(message = "Weight is required")
	@Pattern(regexp = "^[1-9][0-9]{1,2}\\s?kg$", message = "Weight must be like '41 kg', '70 kg', '120 kg'")
	private String weightInKgs;

	public String getWeightInKgs() {
		return weightInKgs;
	}

	public void setWeightInKgs(String weightInKgs) {
		this.weightInKgs = weightInKgs;
	}

}
