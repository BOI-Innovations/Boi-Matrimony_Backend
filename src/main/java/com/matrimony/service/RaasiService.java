package com.matrimony.service;

import com.matrimony.model.dto.request.RaasiRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface RaasiService {

    ResponseEntity createRaasi(RaasiRequest request);

    ResponseEntity updateRaasi(Long id, RaasiRequest request);

    ResponseEntity getRaasiById(Long id);

    ResponseEntity getAllRaasis();

    ResponseEntity deleteRaasi(Long id);

    ResponseEntity uploadRaasiExcel(MultipartFile file);
}
