package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.ProfileFilterRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.enums.Gender;
import com.matrimony.util.MatchService;

@RestController
@RequestMapping("/api/suggestions")
@CrossOrigin(allowedHeaders = "*")
public class SuggestionController {

	@Autowired
	private MatchService matchService;

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/profiles")
	public ResponseEntity getSuggestedProfiles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {

		if (page < 0) {
			return new ResponseEntity("Page must be non-negative", 400, null);
		}
		if (size <= 0 || size > 50) {
			return new ResponseEntity("Size must be between 1 and 50", 400, null);
		}
		return matchService.getSuggestedProfilesForCurrentUser(page, size);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/profiles/sendInvitations")
	public ResponseEntity getPendingMatches(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {

		if (page < 0) {
			return new ResponseEntity("Page must be non-negative", 400, null);
		}
		if (size <= 0 || size > 50) {
			return new ResponseEntity("Size must be between 1 and 50", 400, null);
		}
		return matchService.getPendingMatches(page, size);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/profiles/getInvitations")
	public ResponseEntity getInvitations(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {

		if (page < 0) {
			return new ResponseEntity("Page must be non-negative", 400, null);
		}
		if (size <= 0 || size > 50) {
			return new ResponseEntity("Size must be between 1 and 50", 400, null);
		}
		return matchService.getInvitations(page, size);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/profiles/getAcceptedMatches")
	public ResponseEntity getAcceptedMatches(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {

		if (page < 0) {
			return new ResponseEntity("Page must be non-negative", 400, null);
		}
		if (size <= 0 || size > 50) {
			return new ResponseEntity("Size must be between 1 and 50", 400, null);
		}
		return matchService.getAcceptedMatches(page, size);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/dashboard")
	public ResponseEntity getDashboardSuggestions() {
		return matchService.getDashboardSuggestions();
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/quick")
	public ResponseEntity getQuickSuggestions() {
		return matchService.getQuickSuggestions();
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/match-percentage/{profileId}")
	public ResponseEntity getMatchPercentage(@PathVariable Long profileId) {
		try {
			if (profileId == null || profileId <= 0) {
				return new ResponseEntity("Invalid profile ID", 400, null);
			}
			return matchService.getMatchPercentage(profileId);
		} catch (Exception e) {
			return new ResponseEntity("Error calculating match: " + e.getMessage(), 500, null);
		}
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/profiles/filter")
	public ResponseEntity getFilteredProfiles(@RequestBody ProfileFilterRequest filterRequest) {
		if (filterRequest.getPage() < 0) {
			return new ResponseEntity("Page must be non-negative", HttpStatus.BAD_REQUEST.value(), null);
		}

		if (filterRequest.getSize() <= 0 || filterRequest.getSize() > 50) {
			return new ResponseEntity("Size must be between 1 and 50", HttpStatus.BAD_REQUEST.value(), null);
		}

		if (filterRequest.getGender() != null) {
			try {
				Gender.valueOf(filterRequest.getGender().toUpperCase());
			} catch (IllegalArgumentException e) {
				return new ResponseEntity("Invalid gender value: " + filterRequest.getGender(),
						HttpStatus.BAD_REQUEST.value(), null);
			}
		}

		return matchService.getFilteredProfiles(filterRequest);
	}

}
