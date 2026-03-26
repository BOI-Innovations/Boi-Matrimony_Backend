package com.matrimony.service;

import com.matrimony.model.dto.request.CasteRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CasteService {

    ResponseEntity createOrUpdateCaste(CasteRequest request);

    ResponseEntity getCasteById(Long id);

    ResponseEntity getAllCastes();

    ResponseEntity deleteCaste(Long id);

    ResponseEntity uploadCasteExcel(MultipartFile file);
}
