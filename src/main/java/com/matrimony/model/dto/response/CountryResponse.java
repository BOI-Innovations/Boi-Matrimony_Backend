package com.matrimony.model.dto.response;

import java.util.List;

public class CountryResponse {

	private Long id;
	private String countryName;
	private List<StateResponse> states;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public CountryResponse(Long id, String countryName) {
		super();
		this.id = id;
		this.countryName = countryName;
	}

	public CountryResponse() {
		super();
	}

	public CountryResponse(Long id, String countryName, List<StateResponse> states) {
		super();
		this.id = id;
		this.countryName = countryName;
		this.states = states;
	}

	public List<StateResponse> getStates() {
		return states;
	}

	public void setStates(List<StateResponse> states) {
		this.states = states;
	}

}
