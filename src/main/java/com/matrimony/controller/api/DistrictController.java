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

import com.matrimony.model.dto.request.DistrictRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.DistrictService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/district")
@CrossOrigin(allowedHeaders = "*")
public class DistrictController {

	@Autowired
	private DistrictService districtService;

	@PostMapping
	public ResponseEntity create(@Valid @RequestBody DistrictRequest request) {
		return districtService.createDistrict(request);
	}

	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody DistrictRequest request) {
		return districtService.updateDistrict(id, request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return districtService.getDistrictById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return districtService.getAllDistricts();
	}

	@GetMapping("/state/{stateId}")
	public ResponseEntity getByState(@PathVariable Long stateId) {
		return districtService.getDistrictsByState(stateId);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		return districtService.deleteDistrict(id);
	}

	@PostMapping("/upload/{stateId}")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file, @PathVariable Long stateId) {
		return districtService.uploadDistrictExcel(file, stateId);
	}
}
