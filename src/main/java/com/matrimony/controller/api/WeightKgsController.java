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

import com.matrimony.model.dto.request.WeightKgsRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.WeightKgsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/weight/kgs")
@CrossOrigin(allowedHeaders = "*")
public class WeightKgsController {

	@Autowired
	private WeightKgsService weightKgsService;

	@PostMapping
	public ResponseEntity createOrUpdate(@Valid @RequestBody WeightKgsRequest request) {
		return weightKgsService.createOrUpdateWeight(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return weightKgsService.getWeightById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return weightKgsService.getAllWeights();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		return weightKgsService.deleteWeight(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
		return weightKgsService.uploadWeightExcel(file);
	}
}
