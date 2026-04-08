package com.matrimony.model.dto.response;

import java.time.LocalDateTime;

import com.matrimony.model.enums.HelpRequestStatus;

public class HelpRequestResponse {

	private int id;
	private String name;
	private String email;
	private String subject;
	private String message;
	private HelpRequestStatus status;
	private LocalDateTime createdAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public HelpRequestStatus getStatus() {
		return status;
	}

	public void setStatus(HelpRequestStatus status) {
		this.status = status;
	}

}
