package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.UpdateUserRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(allowedHeaders = "*")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/profiles")
	public ResponseEntity getAllProfiles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		return adminService.getAllProfiles(page, size);
	}

	@GetMapping("/profiles/pending-verification")
	public ResponseEntity getPendingVerificationProfiles() {
		return adminService.getPendingVerificationProfiles();
	}

	@PutMapping("/profiles/{id}/verify")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity verifyProfile(@PathVariable Long id) {
		return adminService.verifyProfile(id);
	}

	@PutMapping("/profiles/{id}/reject")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity rejectProfile(@PathVariable Long id, @RequestParam String reason) {
		return adminService.rejectProfile(id, reason);
	}

	@GetMapping("/stats")
	public ResponseEntity getDashboardStats() {
		return adminService.getDashboardStats();
	}

	@DeleteMapping("/users/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity deleteUser(@PathVariable Long userId) {
		return adminService.deleteUser(userId);
	}

	@PutMapping("/users/{userId}/deactivate")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity deactivateUser(@PathVariable Long userId) {
		return adminService.deactivateUser(userId);
	}

	@PutMapping("/users/{userId}/activate")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity activateUser(@PathVariable Long userId) {
		return adminService.activateUser(userId);
	}

	@GetMapping("/getUsers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getUsers(@RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
		return adminService.getUsers(search, page, limit);
	}

	@GetMapping("/getSuspendedUsers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getSuspendedUsers(@RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
		return adminService.getSuspendedUsers(search, page, limit);
	}

	@GetMapping("/getUsersByDateRange/date-range")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getUsersByDateRange(@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {

		return adminService.getUsersByDateRange(startDate, endDate, page, limit);
	}

	@GetMapping("/getUsersByStatus/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getUsersByStatus(@RequestParam String status, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int limit) {

		return adminService.getUsersByStatus(status, page, limit);
	}

	@GetMapping("/getAllAdmins")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getAllAdmins() {
		return adminService.getAllAdmins();
	}

	@PutMapping("/updateUser/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
		return adminService.updateUser(id, request);
	}

}
