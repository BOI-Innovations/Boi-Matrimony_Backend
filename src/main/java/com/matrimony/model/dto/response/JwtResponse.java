package com.matrimony.model.dto.response;

import java.util.Set;

public class JwtResponse {

	private String token;
	private String type = "Bearer";

	private Long id;
	private String username;
	private String email;
	private String fullName;
	private String phoneNumber;
	private Set<String> roles;
	private String refreshToken;

	public JwtResponse(String token, Long id, String username, String email, String fullName, String phoneNumber,
			Set<String> roles, String refreshToken) {

		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.roles = roles;
		this.refreshToken = refreshToken;
	}

	public JwtResponse(String token, String refreshToken) {
		this.token = token;
		this.refreshToken = refreshToken;
	}

	// Getters & Setters

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	// ✅ Getter & Setter for phoneNumber
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}