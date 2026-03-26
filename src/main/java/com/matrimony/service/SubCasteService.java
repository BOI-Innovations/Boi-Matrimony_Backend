package com.matrimony.service;

import com.matrimony.model.dto.request.SubCasteRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SubCasteService {
    ResponseEntity createOrUpdateSubCaste(SubCasteRequest request);
    ResponseEntity getSubCasteById(Long id);
    ResponseEntity getAllSubCastes();
    ResponseEntity deleteSubCaste(Long id);
    ResponseEntity uploadSubCasteExcel(MultipartFile file);
}
