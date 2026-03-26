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

import com.matrimony.model.dto.request.HeightRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.HeightService;

@RestController
@RequestMapping("/api/heights")
@CrossOrigin(allowedHeaders = "*")
public class HeightController {

	@Autowired
	private HeightService heightService;

	@PostMapping
	public ResponseEntity createOrUpdate(@RequestBody HeightRequest request) {
		return heightService.createOrUpdateHeight(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return heightService.getHeightById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return heightService.getAllHeights();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteById(@PathVariable Long id) {
		return heightService.deleteHeight(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
		return heightService.uploadHeightExcel(file);
	}
}
