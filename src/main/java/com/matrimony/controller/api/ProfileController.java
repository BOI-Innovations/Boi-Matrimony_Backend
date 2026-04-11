package com.matrimony.controller.api;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.matrimony.model.enums.ProfileVerificationStatus;
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

	@PutMapping("/changeProfileStatus/{id}")
	public ResponseEntity updateProfileStatus(@PathVariable Long id, @RequestParam ProfileVerificationStatus status) {
		return profileService.updateProfileStatus(id, status);
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
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) LocalDate startDate,
			@RequestParam(required = false) LocalDate endDate) {
		return profileService.getAllProfiles(page, size, startDate, endDate);
	}

	
	@GetMapping("/getAllProfilesForVerification")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAllProfilesForVerification(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "1") int page, 
            @RequestParam(defaultValue = "10") int limit) {
        return profileService.getAllProfilesForVerification(search, page, limit);
    }
	
	@GetMapping("/profiles-for-verification")
    public ResponseEntity getProfilesForVerificationWithDateRange(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        
        try {
            return profileService.getProfilesForVerification(search, page, limit, fromDate, toDate);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error fetching profiles for verification: " + e.getMessage(), 500, null);
        }
    }
	
	
}
