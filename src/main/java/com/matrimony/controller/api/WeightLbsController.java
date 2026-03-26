package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.WeightLbsRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.WeightLbsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/weight-lbs")
@CrossOrigin(allowedHeaders = "*")
public class WeightLbsController {

    @Autowired
    private WeightLbsService weightLbsService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity createOrUpdate(@Valid @RequestBody WeightLbsRequest request) {
        return weightLbsService.createOrUpdateWeightLbs(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity getById(@PathVariable Long id) {
        return weightLbsService.getWeightLbsById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity getAll() {
        return weightLbsService.getAllWeightLbs();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity delete(@PathVariable Long id) {
        return weightLbsService.deleteWeightLbs(id);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
        return weightLbsService.uploadWeightLbsExcel(file);
    }
}
