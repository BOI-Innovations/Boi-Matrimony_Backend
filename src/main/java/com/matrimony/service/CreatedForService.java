package com.matrimony.service;

import com.matrimony.model.dto.request.CreatedForRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CreatedForService {

    ResponseEntity createOrUpdateCreatedFor(CreatedForRequest request);

    ResponseEntity getCreatedForById(Long id);

    ResponseEntity getAllCreatedFor();

    ResponseEntity deleteCreatedFor(Long id);

    ResponseEntity uploadCreatedForExcel(MultipartFile file);
}
