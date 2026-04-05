package com.matrimony.model.dto.response;

import java.time.LocalDateTime;

import com.matrimony.model.enums.ReportReason;
import com.matrimony.model.enums.ReportStatus;

public class ReportResponse {

	private Long id;

	private Long profileId;

	private String profileName;

	private Long reportedById;

	private String reportedByName;

	private ReportReason reason;

	private String description;

	private ReportStatus status;

	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Long getReportedById() {
		return reportedById;
	}

	public void setReportedById(Long reportedById) {
		this.reportedById = reportedById;
	}

	public String getReportedByName() {
		return reportedByName;
	}

	public void setReportedByName(String reportedByName) {
		this.reportedByName = reportedByName;
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

	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
}