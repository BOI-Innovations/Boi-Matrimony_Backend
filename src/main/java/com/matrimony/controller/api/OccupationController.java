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

import com.matrimony.model.dto.request.OccupationCategoryRequest;
import com.matrimony.model.dto.request.OccupationOptionRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.OccupationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/occupation")
@CrossOrigin(allowedHeaders = "*")
public class OccupationController {

	@Autowired
	private OccupationService occupationService;

	@PostMapping("/category")
	public ResponseEntity createOrUpdateCategory(@Valid @RequestBody OccupationCategoryRequest request) {
		return occupationService.createOrUpdateCategory(request);
	}

	@DeleteMapping("/category/{id}")
	public ResponseEntity deleteCategory(@PathVariable Long id) {
		return occupationService.deleteCategory(id);
	}

	@GetMapping("/category/{id}")
	public ResponseEntity getCategoryById(@PathVariable Long id) {
		return occupationService.getCategoryById(id);
	}

	@GetMapping("/categories")
	public ResponseEntity getAllCategories() {
		return occupationService.getAllCategories();
	}

	@PostMapping("/option")
	public ResponseEntity createOrUpdateOption(@Valid @RequestBody OccupationOptionRequest request) {
		return occupationService.createOrUpdateOption(request);
	}

	@DeleteMapping("/option/{id}")
	public ResponseEntity deleteOption(@PathVariable Long id) {
		return occupationService.deleteOption(id);
	}

	@GetMapping("/options/{categoryId}")
	public ResponseEntity getOptionsByCategory(@PathVariable Long categoryId) {
		return occupationService.getOptionsByCategory(categoryId);
	}

	@PostMapping("/occupation-category/upload")
	public ResponseEntity uploadOccupationCategoryExcel(@RequestParam("file") MultipartFile file) {
		return occupationService.uploadOccupationCategoryExcel(file);
	}

	@PostMapping("/occupation-option/upload")
	public ResponseEntity uploadOccupationOptionExcel(@RequestParam("file") MultipartFile file) {
		return occupationService.uploadOccupationOptionExcel(file);
	}

}
