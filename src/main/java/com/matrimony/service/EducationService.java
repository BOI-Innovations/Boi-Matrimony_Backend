package com.matrimony.service;

import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.EducationCategoryRequest;
import com.matrimony.model.dto.request.EducationOptionRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface EducationService {

	ResponseEntity createOrUpdateCategory(EducationCategoryRequest request);

	ResponseEntity createOrUpdateOption(EducationOptionRequest request);

	ResponseEntity getCategoryById(Long id);

	ResponseEntity getAllCategories();

	ResponseEntity getOptionsByCategory(Long categoryId);

	ResponseEntity deleteCategory(Long id);

	ResponseEntity deleteOption(Long id);

	ResponseEntity uploadEducationCategoryExcel(MultipartFile file);

	ResponseEntity uploadEducationOptionExcel(MultipartFile file);
}
