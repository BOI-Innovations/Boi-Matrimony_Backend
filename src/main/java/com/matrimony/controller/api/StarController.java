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

import com.matrimony.model.dto.request.StarRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.StarService;

@RestController
@RequestMapping("/api/stars")
@CrossOrigin(allowedHeaders = "*")
public class StarController {

	@Autowired
	private StarService starService;

	@PostMapping
	public ResponseEntity createOrUpdate(@RequestBody StarRequest request) {
		return starService.createOrUpdateStar(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return starService.getStarById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return starService.getAllStars();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteById(@PathVariable Long id) {
		return starService.deleteStar(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
		return starService.uploadStarExcel(file);
	}
}
