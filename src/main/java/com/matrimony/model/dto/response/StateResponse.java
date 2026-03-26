package com.matrimony.model.dto.response;

import java.util.List;

public class StateResponse {
	private Long id;
	private String name;
	private Long countryId;
	private List<DistrictResponse> districts;

	public StateResponse(Long id, String name, List<DistrictResponse> districts) {
		this.id = id;
		this.name = name;
		this.countryId = null;
		this.districts = districts;
	}

	public StateResponse(Long id, String name, Long countryId, List<DistrictResponse> districts) {
		this.id = id;
		this.name = name;
		this.countryId = countryId;
		this.districts = districts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public List<DistrictResponse> getDistricts() {
		return districts;
	}

	public void setDistricts(List<DistrictResponse> districts) {
		this.districts = districts;
	}

}
