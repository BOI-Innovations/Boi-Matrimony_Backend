package com.matrimony.service;

import com.matrimony.model.dto.request.EducationOccupationDetailsRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface EducationOccupationDetailsService {

	ResponseEntity getEducationOccupationDetailsForCurrentUser();

	ResponseEntity createEducationOccupationDetailsForCurrentUser(EducationOccupationDetailsRequest request);

	ResponseEntity updateEducationOccupationDetailsForCurrentUser(EducationOccupationDetailsRequest request);

	ResponseEntity deleteEducationOccupationDetailsForCurrentUser();
}
