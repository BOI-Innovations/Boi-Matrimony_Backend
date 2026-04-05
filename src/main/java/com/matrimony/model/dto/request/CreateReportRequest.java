package com.matrimony.model.dto.request;

import com.matrimony.model.enums.ReportReason;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReportRequest {

	@NotNull(message = "Profile id is required")
	private Long profileId;

	@NotNull(message = "Reason is required")
	private ReportReason reason;

	@Size(max = 1000)
	private String description;

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public ReportReason getReason() {
		return reason;
	}

	public void setReason(ReportReason reason) {
		this.reason = reason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}