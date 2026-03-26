package com.matrimony.service;

import com.matrimony.model.dto.request.ProfilePreferenceRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface ProfilePreferenceService {

    ResponseEntity createOrUpdatePreference(ProfilePreferenceRequest request);

    ResponseEntity getPreferenceByLoggedInUser();

    ResponseEntity deletePreference();

	ResponseEntity getPreferenceById(Long id);
}
