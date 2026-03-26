package com.matrimony.service;

import org.springframework.web.multipart.MultipartFile;
import com.matrimony.model.dto.request.WeightLbsRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface WeightLbsService {
    ResponseEntity createOrUpdateWeightLbs(WeightLbsRequest request);
    ResponseEntity getWeightLbsById(Long id);
    ResponseEntity getAllWeightLbs();
    ResponseEntity deleteWeightLbs(Long id);
    ResponseEntity uploadWeightLbsExcel(MultipartFile file);
}
