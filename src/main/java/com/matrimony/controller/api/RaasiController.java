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

import com.matrimony.model.dto.request.RaasiRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.RaasiService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/raasi")
@CrossOrigin(allowedHeaders = "*")
public class RaasiController {

	@Autowired
	private RaasiService raasiService;

	@PostMapping
	public ResponseEntity create(@Valid @RequestBody RaasiRequest request) {
		return raasiService.createRaasi(request);
	}

	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody RaasiRequest request) {
		return raasiService.updateRaasi(id, request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return raasiService.getRaasiById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return raasiService.getAllRaasis();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		return raasiService.deleteRaasi(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
		return raasiService.uploadRaasiExcel(file);
	}
}
