package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.EducationOccupationDetailsRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.EducationOccupationDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/education-occupation")
@CrossOrigin(allowedHeaders = "*")
public class EducationOccupationDetailsController {

	@Autowired
	private EducationOccupationDetailsService service;

	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ResponseEntity getCurrentUserDetails() {
		return service.getEducationOccupationDetailsForCurrentUser();
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping
	public ResponseEntity createDetails(@Valid @RequestBody EducationOccupationDetailsRequest request) {
		return service.createEducationOccupationDetailsForCurrentUser(request);
	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping
	public ResponseEntity updateDetails(@Valid @RequestBody EducationOccupationDetailsRequest request) {
		return service.updateEducationOccupationDetailsForCurrentUser(request);
	}

	@PreAuthorize("hasRole('USER')")
	@DeleteMapping
	public ResponseEntity deleteDetails() {
		return service.deleteEducationOccupationDetailsForCurrentUser();
	}
}
