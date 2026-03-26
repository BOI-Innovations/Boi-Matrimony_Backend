package com.matrimony.service;

import com.matrimony.model.dto.request.ProfileRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface ProfileService {
	ResponseEntity getCurrentUserProfile();

	ResponseEntity getProfileById(Long id);

	ResponseEntity getProfileByUserId(Long userId);

	ResponseEntity createProfile(ProfileRequest profileRequest);

//	ResponseEntity updateProfile(Long id, ProfileRequest profileRequest);

	ResponseEntity deleteProfile(Long id);

	ResponseEntity calculateProfileCompletion(Long profileId);

	ResponseEntity getLocationAndOccupation(Long profileId);

	ResponseEntity updateProfile(ProfileRequest profileRequest);

	Long getCurrentUserId();

}