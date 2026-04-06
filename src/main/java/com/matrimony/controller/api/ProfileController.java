package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.ProfileRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.ProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

	@Autowired
	private ProfileService profileService;

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getMyProfile() {
		return profileService.getCurrentUserProfile();
	}

	@GetMapping("/{id}")
	public ResponseEntity getProfileById(@PathVariable Long id) {
		return profileService.getProfileById(id);
	}

	@GetMapping("/getLocationAndOccupation/{profileId}")
	public ResponseEntity getLocationAndOccupation(@PathVariable Long profileId) {
		return profileService.getLocationAndOccupation(profileId);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity createProfile(@Valid @RequestBody ProfileRequest profileRequest) {
		return profileService.createProfile(profileRequest);
	}

	@PutMapping("/update")
	public ResponseEntity upsertProfile(@RequestBody ProfileRequest request) {
		return profileService.updateProfile(request);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity deleteProfile(@PathVariable Long id) {
		return profileService.deleteProfile(id);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity getProfileByUserId(@PathVariable Long userId) {
		return profileService.getProfileByUserId(userId);
	}

	@GetMapping("/getAllProfiles")
	public ResponseEntity getAllProfiles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return profileService.getAllProfiles(page, size);
	}

}
