package com.matrimony.model.dto.request;

import com.matrimony.model.enums.PhotoVisibility;
import com.matrimony.model.enums.ProfileVisibility;

public class PrivacySettingsRequest {

	private ProfileVisibility profileVisibility;
	private PhotoVisibility showPhotosTo;
	private Boolean hideProfile;

	// Getters and Setters

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