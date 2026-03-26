
package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.matrimony.model.dto.request.LocationRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.LocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(allowedHeaders = "*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMyLocation() {
        return locationService.getLocationForCurrentUser();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity createLocation(@Valid @RequestBody LocationRequest request) {
        return locationService.createLocationForCurrentUser(request);
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateLocation(@Valid @RequestBody LocationRequest request) {
        return locationService.updateLocationForCurrentUser(request);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteLocation() {
        return locationService.deleteLocationForCurrentUser();
    }
}
