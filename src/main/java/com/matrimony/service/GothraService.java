package com.matrimony.service;

import com.matrimony.model.dto.request.GothraRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GothraService {
    ResponseEntity createOrUpdateGothra(GothraRequest request);
    ResponseEntity getGothraById(Long id);
    ResponseEntity getAllGothras();
    ResponseEntity deleteGothra(Long id);
    ResponseEntity uploadGothraExcel(MultipartFile file);
}
