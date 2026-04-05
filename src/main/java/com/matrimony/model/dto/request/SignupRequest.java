package com.matrimony.model.dto.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

	public class SignupRequest {
	
		@NotBlank(message = "Username is required")
		@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
		private String username;
	
		@NotBlank(message = "Email is required")
		@Size(max = 50, message = "Email must be at most 50 characters")
		@Email(message = "Email should be valid")
		private String email;
	
		@NotBlank(message = "Password is required")
		@Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
		private String password;
	
		private Set<String> roles;
	
		@NotBlank(message = "Phone number is required")
		@Size(max = 15, message = "Phone number must be at most 15 digits")
		@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be valid")
		private String phoneNumber;

	// Getters and Setters (with auto-trim)

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
	}
}
