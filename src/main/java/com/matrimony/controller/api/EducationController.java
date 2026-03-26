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

import com.matrimony.model.dto.request.EducationCategoryRequest;
import com.matrimony.model.dto.request.EducationOptionRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.EducationService;

@RestController
@RequestMapping("/api/education")
@CrossOrigin(allowedHeaders = "*")
public class EducationController {

	@Autowired
	private EducationService educationService;

	@PostMapping("/category")
	public ResponseEntity createOrUpdateCategory(@RequestBody EducationCategoryRequest request) {
		return educationService.createOrUpdateCategory(request);
	}

	@PostMapping("/option")
	public ResponseEntity createOrUpdateOption(@RequestBody EducationOptionRequest request) {
		return educationService.createOrUpdateOption(request);
	}

	@GetMapping("/category/{id}")
	public ResponseEntity getCategoryById(@PathVariable Long id) {
		return educationService.getCategoryById(id);
	}

	@GetMapping("/categories")
	public ResponseEntity getAllCategories() {
		return educationService.getAllCategories();
	}

	@GetMapping("/options/{categoryId}")
	public ResponseEntity getOptionsByCategory(@PathVariable Long categoryId) {
		return educationService.getOptionsByCategory(categoryId);
	}

	@DeleteMapping("/category/{id}")
	public ResponseEntity deleteCategory(@PathVariable Long id) {
		return educationService.deleteCategory(id);
	}

	@DeleteMapping("/option/{id}")
	public ResponseEntity deleteOption(@PathVariable Long id) {
		return educationService.deleteOption(id);
	}

	@PostMapping("/education-category/upload")
	public ResponseEntity uploadEducationCategoryExcel(@RequestParam("file") MultipartFile file) {
		return educationService.uploadEducationCategoryExcel(file);
	}

	@PostMapping("/education-option/upload")
	public ResponseEntity uploadEducationOptionExcel(@RequestParam("file") MultipartFile file) {
		return educationService.uploadEducationOptionExcel(file);
	}

}
