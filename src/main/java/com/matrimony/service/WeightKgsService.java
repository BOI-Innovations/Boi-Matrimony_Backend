package com.matrimony.service;

import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.WeightKgsRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface WeightKgsService {
	ResponseEntity createOrUpdateWeight(WeightKgsRequest request);

	ResponseEntity getWeightById(Long id);

	ResponseEntity getAllWeights();

	ResponseEntity deleteWeight(Long id);

	ResponseEntity uploadWeightExcel(MultipartFile file);
}
