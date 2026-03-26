package com.matrimony.service;

import com.matrimony.model.dto.request.HeightRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface HeightService {
    ResponseEntity createOrUpdateHeight(HeightRequest request);
    ResponseEntity getHeightById(Long id);
    ResponseEntity getAllHeights();
    ResponseEntity deleteHeight(Long id);
    ResponseEntity uploadHeightExcel(MultipartFile file);
}
