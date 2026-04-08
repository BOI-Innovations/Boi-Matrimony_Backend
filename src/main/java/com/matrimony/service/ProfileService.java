package com.matrimony.service;

import java.time.LocalDate;

import com.matrimony.model.dto.request.ProfileRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.enums.ProfileVerificationStatus;

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

	ResponseEntity getAllProfiles(int page, int size, LocalDate startDate, LocalDate endDate);

	ResponseEntity updateProfileStatus(Long id, ProfileVerificationStatus status);

}