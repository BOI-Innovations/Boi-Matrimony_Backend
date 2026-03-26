package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.CommunicationSettingsRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.CommunicationSettingsService;

@RestController
@RequestMapping("/api/communication-settings")
public class CommunicationSettingsController {

	@Autowired
	private CommunicationSettingsService communicationSettingsService;

	@PostMapping
	public ResponseEntity saveSettings(@RequestBody CommunicationSettingsRequest request) {
		return communicationSettingsService.saveSettings(request);
	}

	@GetMapping
	public ResponseEntity getSettings() {
		return communicationSettingsService.getSettings();
	}

	@PutMapping
	public ResponseEntity updateSettings(@RequestBody CommunicationSettingsRequest request) {
		return communicationSettingsService.updateSettings(request);
	}
}
