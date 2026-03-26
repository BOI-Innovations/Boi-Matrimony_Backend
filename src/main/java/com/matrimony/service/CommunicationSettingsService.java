package com.matrimony.service;

import com.matrimony.model.dto.request.CommunicationSettingsRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface CommunicationSettingsService {

	ResponseEntity saveSettings(CommunicationSettingsRequest request);

	ResponseEntity getSettings();

	ResponseEntity updateSettings(CommunicationSettingsRequest request);
}
