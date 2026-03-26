package com.matrimony.controller.api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.enums.RoleName;
import com.matrimony.service.UserService;
import com.matrimony.serviceImpl.ProfileServiceImpl;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(allowedHeaders = "*")
public class UserController {

    private final SearchController searchController;
	
	@Autowired
	ProfileServiceImpl profileServiceImpl;

	@Autowired
	private UserService userService;

    UserController(SearchController searchController) {
        this.searchController = searchController;
    }

	@GetMapping("/{id}")
	public ResponseEntity getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	@PostMapping("/verify-email")
	public ResponseEntity verifyEmail(@RequestParam String token) {
		return userService.verifyEmail(token);
	}

	@PostMapping("/request-password-reset")
	public ResponseEntity requestPasswordReset(@RequestParam String email) {
		return userService.requestPasswordReset(email);
	}

	@PostMapping("/reset-password")
	public ResponseEntity resetPassword(@RequestParam String token, @RequestParam String newPassword) {
		return userService.resetPassword(token, newPassword.trim());
	}

	@PostMapping("/change-password")
	public ResponseEntity changePassword(@RequestParam String username, @RequestParam String oldPassword,
			@RequestParam String newPassword) {
		return userService.changePassword(username.trim(), oldPassword.trim(), newPassword.trim());
	}

	@PostMapping("/{id}/update-last-login")
	public ResponseEntity updateLastLogin(@PathVariable Long id) {
		return userService.updateLastLogin(id);
	}

	@GetMapping("/by-active-status")
	public ResponseEntity getUsersByIsActive(@RequestParam Boolean isActive) {
		return userService.getUsersByIsActive(isActive);
	}

	@GetMapping("/by-active-status-paged")
	public ResponseEntity getUsersByIsActivePaged(@RequestParam Boolean isActive, Pageable pageable) {
		return userService.getUsersByIsActive(isActive, pageable);
	}

	@GetMapping("/by-email-verified")
	public ResponseEntity getUsersByEmailVerified(@RequestParam Boolean emailVerified) {
		return userService.getUsersByEmailVerified(emailVerified);
	}

	@GetMapping("/by-role")
	public ResponseEntity getUsersByRole(@RequestParam RoleName role) {
		return userService.getUsersByRole(role);
	}

	@GetMapping("/registered-between")
	public ResponseEntity getUsersRegisteredBetween(@RequestParam LocalDateTime startDate,
			@RequestParam LocalDateTime endDate) {
		return userService.getUsersRegisteredBetween(startDate, endDate);
	}

	@GetMapping("/inactive-since")
	public ResponseEntity getInactiveUsersSince(@RequestParam LocalDateTime since) {
		return userService.getInactiveUsersSince(since);
	}

	@GetMapping("/count-active")
	public ResponseEntity countActiveUsers() {
		return userService.countActiveUsers();
	}

	@PostMapping("/{id}/status")
	public ResponseEntity updateUserStatus(@PathVariable Long id, @RequestParam Boolean isActive) {
		return userService.updateUserStatus(id, isActive);
	}

	@GetMapping("/search")
	public ResponseEntity searchUsers(@RequestParam String keyword) {
		return userService.searchUsers(keyword);
	}

	@PostMapping("/send-otp")
	public ResponseEntity sendOTP(@RequestParam String email) {
		return userService.sendOTP(email.trim());
	}

	@PostMapping("/send-otp-for-sign-up")
	public ResponseEntity sendOTPForSignUp(@RequestParam String email) {
		return userService.sendOTPForSignUp(email.trim());
	}

	@PostMapping("/verify-otp")
	public ResponseEntity verifyOtp(@RequestParam String email, @RequestParam int otp) {
		return userService.verifyOTP(email.trim(), otp);
	}

	@PostMapping("/verify-otp-for-forgot-password")
	public ResponseEntity verifyOTPForForgotPassword(@RequestParam String email, @RequestParam int otp) {
		return userService.verifyOTPForForgotPassword(email.trim(), otp);
	}

	@PostMapping("/update-credentials")
	public ResponseEntity updateCredentials(@RequestParam String oldUsername,
			@RequestParam(required = false) String newUsername, @RequestParam(required = false) String newEmail) {
		return userService.updateCredentials(oldUsername.trim(), newUsername, newEmail);
	}
	
	@PostMapping("/deactivateUser")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity deactivateUser(@RequestParam Long userId) {
		return userService.deactivateUser(userId);
	}
	
	@PostMapping("/activateUser")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity activateUser(@RequestParam Long userId) {
		return userService.activateUser(userId);
	}

	@DeleteMapping("/deleteUser")
	public ResponseEntity deleteUser(@RequestParam Long userId) {
		return userService.deleteUser(userId);
	}
	
	@DeleteMapping("/deleteAccount")
	public ResponseEntity deleteAccount() {
		Long userId = profileServiceImpl.getCurrentUserId();
		return userService.deleteUser(userId);
	}
}
