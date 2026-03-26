package com.matrimony.service;

import com.matrimony.model.dto.request.StarRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StarService {
    ResponseEntity createOrUpdateStar(StarRequest request);
    ResponseEntity getStarById(Long id);
    ResponseEntity getAllStars();
    ResponseEntity deleteStar(Long id);
    ResponseEntity uploadStarExcel(MultipartFile file);
}
