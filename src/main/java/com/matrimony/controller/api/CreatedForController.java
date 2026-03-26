package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.CreatedForRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.CreatedForService;

@RestController
@RequestMapping("/api/created-for")
@CrossOrigin(allowedHeaders = "*")
class CreatedForController {

	@Autowired
	private CreatedForService createdForService;

	@PostMapping
	public ResponseEntity createOrUpdateCreatedFor(@RequestBody CreatedForRequest request) {
		return createdForService.createOrUpdateCreatedFor(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getCreatedForById(@PathVariable Long id) {
		return createdForService.getCreatedForById(id);
	}

	@GetMapping
	public ResponseEntity getAllCreatedFor() {
		return createdForService.getAllCreatedFor();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteCreatedFor(@PathVariable Long id) {
		return createdForService.deleteCreatedFor(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadCreatedForExcel(@RequestParam MultipartFile file) {
		return createdForService.uploadCreatedForExcel(file);
	}
}