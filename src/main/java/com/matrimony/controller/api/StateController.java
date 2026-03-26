package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.StateRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.StateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/state")
@CrossOrigin(allowedHeaders = "*")
public class StateController {

	@Autowired
	private StateService stateService;

	@PostMapping
	public ResponseEntity create(@Valid @RequestBody StateRequest request) {
		return stateService.createState(request);
	}

	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody StateRequest request) {
		return stateService.updateState(id, request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return stateService.getStateById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return stateService.getAllStates();
	}

	@GetMapping("/country/{countryId}")
	public ResponseEntity getByCountry(@PathVariable Long countryId) {
		return stateService.getStatesByCountry(countryId);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		return stateService.deleteState(id);
	}

	@PostMapping("/upload/{countryId}")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file, @PathVariable Long countryId) {
		return stateService.uploadStateExcel(file, countryId);
	}
}
