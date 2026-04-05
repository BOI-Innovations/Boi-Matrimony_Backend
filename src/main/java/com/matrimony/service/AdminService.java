package com.matrimony.service;

import com.matrimony.model.dto.request.UpdateUserRequest;
import com.matrimony.model.entity.ResponseEntity;

import jakarta.validation.Valid;

public interface AdminService {
	ResponseEntity getAllProfiles(int page, int size);

	ResponseEntity getPendingVerificationProfiles();

	ResponseEntity verifyProfile(Long profileId);

	ResponseEntity rejectProfile(Long profileId, String reason);

	ResponseEntity getDashboardStats();

	ResponseEntity deleteUser(Long userId);

	ResponseEntity deactivateUser(Long userId);

	ResponseEntity activateUser(Long userId);

	ResponseEntity getUsers(String search, int page, int limit);

	ResponseEntity getUsersByDateRange(String startDate, String endDate, int page, int limit);

	ResponseEntity getUsersByStatus(String status, int page, int limit);

	ResponseEntity getSuspendedUsers(String search, int page, int limit);

	ResponseEntity getAllAdmins();

	ResponseEntity updateUser(Long id, @Valid UpdateUserRequest request);
}