package com.matrimony.service;

import com.matrimony.model.dto.request.DistrictRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface DistrictService {

    ResponseEntity createDistrict(DistrictRequest request);

    ResponseEntity updateDistrict(Long id, DistrictRequest request);

    ResponseEntity getDistrictById(Long id);

    ResponseEntity getAllDistricts();

    ResponseEntity getDistrictsByState(Long stateId);

    ResponseEntity deleteDistrict(Long id);

    ResponseEntity uploadDistrictExcel(MultipartFile file, Long stateId);
}
