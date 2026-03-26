package com.matrimony.model.dto.response;

public class DistrictResponse {

	private Long id;
	private String name;
	private Long stateId;

	public DistrictResponse(Long id, String name) {
		this.id = id;
		this.name = name;
		this.stateId = null;
	}

	public DistrictResponse(Long id, String name, Long stateId) {
		this.id = id;
		this.name = name;
		this.stateId = stateId;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getStateId() {
		return stateId;
	}
}
