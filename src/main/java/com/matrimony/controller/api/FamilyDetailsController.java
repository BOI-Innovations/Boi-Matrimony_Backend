package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.matrimony.model.dto.request.FamilyDetailsRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.FamilyDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/family")
@CrossOrigin(allowedHeaders = "*")
public class FamilyDetailsController {

    @Autowired
    private FamilyDetailsService familyDetailsService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMyFamilyDetails() {
        return familyDetailsService.getFamilyDetailsForCurrentUser();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity createFamilyDetails(@Valid @RequestBody FamilyDetailsRequest request) {
        return familyDetailsService.createFamilyDetailsForCurrentUser(request);
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateFamilyDetails(@Valid @RequestBody FamilyDetailsRequest request) {
        return familyDetailsService.updateFamilyDetailsForCurrentUser(request);
    }
    
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/parents/contact")
    public ResponseEntity updateParentsContactNumber(@RequestParam String contactDetails) {
        return familyDetailsService.updateParentsContactNumber(contactDetails);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteFamilyDetails() {
        return familyDetailsService.deleteFamilyDetailsForCurrentUser();
    }
}
