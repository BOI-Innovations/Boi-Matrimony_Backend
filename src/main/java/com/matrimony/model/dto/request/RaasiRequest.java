package com.matrimony.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RaasiRequest {

	@NotBlank(message = "Raasi name is required")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
