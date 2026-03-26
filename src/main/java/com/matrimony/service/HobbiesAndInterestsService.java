package com.matrimony.service;

import com.matrimony.model.dto.request.HobbiesAndInterestsRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface HobbiesAndInterestsService {

	ResponseEntity getHobbiesAndInterestsForCurrentUser();

	ResponseEntity createHobbiesAndInterestsForCurrentUser(HobbiesAndInterestsRequest request);

	ResponseEntity updateHobbiesAndInterestsForCurrentUser(HobbiesAndInterestsRequest request);

	ResponseEntity deleteHobbiesAndInterestsForCurrentUser();
}
