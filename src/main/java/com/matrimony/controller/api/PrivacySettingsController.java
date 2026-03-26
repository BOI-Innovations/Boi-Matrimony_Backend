package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.matrimony.model.dto.request.PrivacySettingsRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.PrivacySettingsService;

@RestController
@RequestMapping("/api/privacy-settings")
@CrossOrigin(allowedHeaders = "*")
public class PrivacySettingsController {

    @Autowired
    private PrivacySettingsService privacySettingsService;

    @PostMapping
    public ResponseEntity saveSettings(@RequestBody PrivacySettingsRequest request) {
        return privacySettingsService.saveSettings(request);
    }

    @GetMapping
    public ResponseEntity getSettings() {
        return privacySettingsService.getSettings();
    }

    @PutMapping
    public ResponseEntity updateSettings(@RequestBody PrivacySettingsRequest request) {
        return privacySettingsService.updateSettings(request);
    }
}
