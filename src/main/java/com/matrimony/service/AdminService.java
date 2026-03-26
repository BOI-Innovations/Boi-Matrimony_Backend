package com.matrimony.service;

import com.matrimony.model.entity.ResponseEntity;

public interface AdminService {
	ResponseEntity getAllProfiles(int page, int size);

	ResponseEntity getPendingVerificationProfiles();

	ResponseEntity verifyProfile(Long profileId);

	ResponseEntity rejectProfile(Long profileId, String reason);

	ResponseEntity getDashboardStats();

	ResponseEntity deleteUser(Long userId);

	ResponseEntity deactivateUser(Long userId);

	ResponseEntity activateUser(Long userId);
}