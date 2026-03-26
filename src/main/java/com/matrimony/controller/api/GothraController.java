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

import com.matrimony.model.dto.request.GothraRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.GothraService;

@RestController
@RequestMapping("/api/gothras")
@CrossOrigin(allowedHeaders = "*")
public class GothraController {

	@Autowired
	private GothraService gothraService;

	@PostMapping
	public ResponseEntity createOrUpdate(@RequestBody GothraRequest request) {
		return gothraService.createOrUpdateGothra(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return gothraService.getGothraById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return gothraService.getAllGothras();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteById(@PathVariable Long id) {
		return gothraService.deleteGothra(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
		return gothraService.uploadGothraExcel(file);
	}
}
