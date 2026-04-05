package com.matrimony.model.dto.request;

import com.matrimony.model.enums.ReportActionType;

import jakarta.validation.constraints.NotNull;

public class ReportActionRequest {

	@NotNull(message = "Action is required")
	private ReportActionType action;

	private String notes;

	public ReportActionType getAction() {
		return action;
	}

	public void setAction(ReportActionType action) {
		this.action = action;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}