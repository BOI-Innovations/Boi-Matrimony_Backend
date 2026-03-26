package com.matrimony.service;

import com.matrimony.model.dto.request.MotherTongueRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface MotherTongueService {

    ResponseEntity createOrUpdateMotherTongue(MotherTongueRequest request);

    ResponseEntity getMotherTongueById(Long id);

    ResponseEntity getAllMotherTongues();

    ResponseEntity deleteMotherTongue(Long id);

    ResponseEntity uploadMotherTongueExcel(MultipartFile file);
}
