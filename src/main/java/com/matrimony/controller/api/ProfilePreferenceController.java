package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.matrimony.model.dto.request.ProfilePreferenceRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.ProfilePreferenceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile-preference")
public class ProfilePreferenceController {

    @Autowired
    private ProfilePreferenceService preferenceService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/save")
    public ResponseEntity createOrUpdatePreference(@Valid @RequestBody ProfilePreferenceRequest request) {
        return preferenceService.createOrUpdatePreference(request);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity getPreference() {
        return preferenceService.getPreferenceByLoggedInUser();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity getPreferenceById(@PathVariable Long id) {
        return preferenceService.getPreferenceById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete")
    public ResponseEntity deletePreference() {
        return preferenceService.deletePreference();
    }
}
