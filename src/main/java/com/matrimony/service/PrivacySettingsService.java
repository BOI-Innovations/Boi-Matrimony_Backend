package com.matrimony.service;

import com.matrimony.model.dto.request.PrivacySettingsRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;

public interface PrivacySettingsService {

    ResponseEntity saveSettings(PrivacySettingsRequest request);

    ResponseEntity getSettings();

    ResponseEntity updateSettings(PrivacySettingsRequest request);

	boolean canViewPhoto(User viewer, User owner);
}
