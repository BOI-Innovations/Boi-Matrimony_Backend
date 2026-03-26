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

import com.matrimony.model.dto.request.SubCasteRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.SubCasteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sub-caste")
@CrossOrigin(allowedHeaders = "*")
public class SubCasteController {

	@Autowired
	private SubCasteService subCasteService;

	@PostMapping
	public ResponseEntity createOrUpdate(@Valid @RequestBody SubCasteRequest request) {
		return subCasteService.createOrUpdateSubCaste(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return subCasteService.getSubCasteById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return subCasteService.getAllSubCastes();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		return subCasteService.deleteSubCaste(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
		return subCasteService.uploadSubCasteExcel(file);
	}
}
