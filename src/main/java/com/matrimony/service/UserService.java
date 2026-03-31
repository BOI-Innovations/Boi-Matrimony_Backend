package com.matrimony.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.matrimony.model.dto.request.HelpRequestRequest;
import com.matrimony.model.dto.request.SignupRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.RoleName;

public interface UserService {

	ResponseEntity createUser(SignupRequest signUpRequest);

	ResponseEntity getUserById(Long id);

	ResponseEntity deactivateUser(Long userId);

	ResponseEntity activateUser(Long userId);

	ResponseEntity updateLastLogin(Long userId);

	ResponseEntity verifyEmail(String token);

	ResponseEntity requestPasswordReset(String email);

	ResponseEntity resetPassword(String token, String newPassword);

	Object getUserByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	User getUserByUsername(String username);

	User save(User user);

	ResponseEntity getUsersByIsActive(Boolean isActive);

	ResponseEntity getUsersByEmailVerified(Boolean emailVerified);

	ResponseEntity getUsersByRole(RoleName role);

	ResponseEntity getUsersByIsActive(Boolean isActive, Pageable pageable);

	ResponseEntity getInactiveUsersSince(LocalDateTime date);

	ResponseEntity getUsersRegisteredBetween(LocalDateTime startDate, LocalDateTime endDate);

	ResponseEntity countActiveUsers();

	ResponseEntity updateUserStatus(Long userId, Boolean isActive);

	ResponseEntity searchUsers(String keyword);

	ResponseEntity sendOTP(String email);

	ResponseEntity verifyOTP(String email, int enteredOtp);

	ResponseEntity sendOTPForSignUp(String email);

	ResponseEntity sendWelcomeEmail(String email);

	ResponseEntity verifyOTPForForgotPassword(String email, int enteredOtp);

	public ResponseEntity changePassword(String username, String oldPassword, String newPassword);
	ResponseEntity updateCredentials(String oldUserName, String newUsername, String newEmail);

	Optional<User> findById(Long id);

	ResponseEntity deleteUser(Long userId);

	ResponseEntity updateCredentials(String oldUsername, String newUsername, String newEmail, String newPhoneNumber);

	ResponseEntity getUsers(String search, int page, int limit);

	ResponseEntity createHelpRequest(HelpRequestRequest request);
}