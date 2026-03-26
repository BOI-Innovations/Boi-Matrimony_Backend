package com.matrimony.service;

import com.matrimony.model.dto.request.StateRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StateService {

    ResponseEntity createState(StateRequest request);

    ResponseEntity updateState(Long id, StateRequest request);

    ResponseEntity getStateById(Long id);

    ResponseEntity getAllStates();

    ResponseEntity getStatesByCountry(Long countryId);

    ResponseEntity deleteState(Long id);

    ResponseEntity uploadStateExcel(MultipartFile file, Long countryId);
}
