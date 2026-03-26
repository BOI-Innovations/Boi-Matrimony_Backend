package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(allowedHeaders = "*")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/profiles")
	public ResponseEntity getAllProfiles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
				System.out.println("Hello Java");
				System.out.println("Hello Java");
		return adminService.getAllProfiles(page, size);
	}

	@GetMapping("/profiles/pending-verification")
	public ResponseEntity getPendingVerificationProfiles() {
		return adminService.getPendingVerificationProfiles();
	}

	@PutMapping("/profiles/{id}/verify")
	public ResponseEntity verifyProfile(@PathVariable Long id) {
		return adminService.verifyProfile(id);
	}

	@PutMapping("/profiles/{id}/reject")
	public ResponseEntity rejectProfile(@PathVariable Long id, @RequestParam String reason) {
		return adminService.rejectProfile(id, reason);
	}

	@GetMapping("/stats")
	public ResponseEntity getDashboardStats() {
		return adminService.getDashboardStats();
	}

	@DeleteMapping("/users/{userId}")
	public ResponseEntity deleteUser(@PathVariable Long userId) {
		return adminService.deleteUser(userId);
	}

	@PutMapping("/users/{userId}/deactivate")
	public ResponseEntity deactivateUser(@PathVariable Long userId) {
		return adminService.deactivateUser(userId);
	}

	@PutMapping("/users/{userId}/activate")
	public ResponseEntity activateUser(@PathVariable Long userId) {
		return adminService.activateUser(userId);
	}
}
