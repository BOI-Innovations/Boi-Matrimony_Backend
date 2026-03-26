package com.matrimony.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrimony.model.enums.PhotoVisibility;
import com.matrimony.model.enums.ProfileVisibility;

import jakarta.persistence.*;

@Entity
@Table(name = "privacy_settings")
public class PrivacySettings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	@JsonIgnore
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private ProfileVisibility profileVisibility;

	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private PhotoVisibility showPhotosTo;

	private Boolean hideProfile;

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ProfileVisibility getProfileVisibility() {
		return profileVisibility;
	}

	public void setProfileVisibility(ProfileVisibility profileVisibility) {
		this.profileVisibility = profileVisibility;
	}


	public PhotoVisibility getShowPhotosTo() {
		return showPhotosTo;
	}

	public void setShowPhotosTo(PhotoVisibility showPhotosTo) {
		this.showPhotosTo = showPhotosTo;
	}


	public Boolean getHideProfile() {
		return hideProfile;
	}

	public void setHideProfile(Boolean hideProfile) {
		this.hideProfile = hideProfile;
	}
}