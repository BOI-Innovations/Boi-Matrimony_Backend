package com.matrimony.controller.api;

import com.matrimony.model.dto.request.InterestRequest;
import com.matrimony.model.dto.response.ApiResponse;
import com.matrimony.model.dto.response.InterestResponse;
import com.matrimony.service.InterestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
@CrossOrigin(allowedHeaders = "*")
public class InterestController {

    @Autowired
    private InterestService interestService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InterestResponse> sendInterest(@Valid @RequestBody InterestRequest interestRequest) {
        InterestResponse response = interestService.sendInterest(interestRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sent")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<InterestResponse>> getSentInterests() {
        List<InterestResponse> interests = interestService.getSentInterests();
        return ResponseEntity.ok(interests);
    }

    @GetMapping("/received")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<InterestResponse>> getReceivedInterests() {
        List<InterestResponse> interests = interestService.getReceivedInterests();
        return ResponseEntity.ok(interests);
    }

    @PutMapping("/{id}/respond")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InterestResponse> respondToInterest(@PathVariable Long id, 
                                                            @RequestParam String status) {
        InterestResponse response = interestService.respondToInterest(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> withdrawInterest(@PathVariable Long id) {
        interestService.withdrawInterest(id);
        return ResponseEntity.ok(new ApiResponse(true, "Interest withdrawn successfully"));
    }

    @GetMapping("/mutual")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<InterestResponse>> getMutualInterests() {
        List<InterestResponse> mutualInterests = interestService.getMutualInterests();
        return ResponseEntity.ok(mutualInterests);
    }
}