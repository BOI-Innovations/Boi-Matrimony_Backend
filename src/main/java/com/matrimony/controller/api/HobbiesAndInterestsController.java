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

import com.matrimony.model.dto.request.HobbiesAndInterestsRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.HobbiesAndInterestsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hobbies")
@CrossOrigin(allowedHeaders = "*")
public class HobbiesAndInterestsController {

	@Autowired
	private HobbiesAndInterestsService hobbiesAndInterestsService;

	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getMyHobbiesAndInterests() {
		return hobbiesAndInterestsService.getHobbiesAndInterestsForCurrentUser();
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity createHobbiesAndInterests(@Valid @RequestBody HobbiesAndInterestsRequest request) {
		System.out.println("Request is" + request.toString());
		return hobbiesAndInterestsService.createHobbiesAndInterestsForCurrentUser(request);
	}

	@PutMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity updateHobbiesAndInterests(@Valid @RequestBody HobbiesAndInterestsRequest request) {
		return hobbiesAndInterestsService.updateHobbiesAndInterestsForCurrentUser(request);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity deleteHobbiesAndInterests() {
		return hobbiesAndInterestsService.deleteHobbiesAndInterestsForCurrentUser();
	}
}
