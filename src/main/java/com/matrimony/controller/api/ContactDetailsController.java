package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.ContactDetailsRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.ContactDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(allowedHeaders = "*")
public class ContactDetailsController {

	@Autowired
	private ContactDetailsService contactDetailsService;

	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getMyContactDetails() {
		return contactDetailsService.getContactDetailsForCurrentUser();
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity createContactDetails(@Valid @RequestBody ContactDetailsRequest request) {
		return contactDetailsService.createContactDetailsForCurrentUser(request);
	}

	@PutMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity updateContactDetails(@Valid @RequestBody ContactDetailsRequest request) {
		return contactDetailsService.updateContactDetailsForCurrentUser(request);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity deleteContactDetails() {
		return contactDetailsService.deleteContactDetailsForCurrentUser();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getContactDetailsById(@PathVariable Long id) {
		return contactDetailsService.getContactDetailsById(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity updateContactDetailsById(@PathVariable Long id,
			@Valid @RequestBody ContactDetailsRequest request) {
		return contactDetailsService.updateContactDetailsById(id, request);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity deleteContactDetailsById(@PathVariable Long id) {
		return contactDetailsService.deleteContactDetailsById(id);
	}
}
