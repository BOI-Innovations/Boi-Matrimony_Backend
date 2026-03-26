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

import com.matrimony.model.dto.request.MotherTongueRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.MotherTongueService;

@RestController
@RequestMapping("/api/mother-tongues")
@CrossOrigin(allowedHeaders = "*")
public class MotherTongueController {

	@Autowired
	private MotherTongueService motherTongueService;

	@PostMapping
	public ResponseEntity createOrUpdateMotherTongue(@RequestBody MotherTongueRequest request) {
		return motherTongueService.createOrUpdateMotherTongue(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getMotherTongueById(@PathVariable Long id) {
		return motherTongueService.getMotherTongueById(id);
	}

	@GetMapping
	public ResponseEntity getAllMotherTongues() {
		return motherTongueService.getAllMotherTongues();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteMotherTongue(@PathVariable Long id) {
		return motherTongueService.deleteMotherTongue(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadMotherTongueExcel(@RequestParam("file") MultipartFile file) {
		return motherTongueService.uploadMotherTongueExcel(file);
	}
}
