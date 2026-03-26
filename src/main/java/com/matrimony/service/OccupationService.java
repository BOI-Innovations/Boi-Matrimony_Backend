package com.matrimony.service;

import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.OccupationCategoryRequest;
import com.matrimony.model.dto.request.OccupationOptionRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface OccupationService {

    ResponseEntity createOrUpdateCategory(OccupationCategoryRequest request);

    ResponseEntity createOrUpdateOption(OccupationOptionRequest request);

    ResponseEntity getCategoryById(Long id);

    ResponseEntity getAllCategories();

    ResponseEntity getOptionsByCategory(Long categoryId);

    ResponseEntity deleteCategory(Long id);

    ResponseEntity deleteOption(Long id);

	ResponseEntity uploadOccupationCategoryExcel(MultipartFile file);

	ResponseEntity uploadOccupationOptionExcel(MultipartFile file);
}
