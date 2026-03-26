package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.matrimony.model.dto.request.UserSubscriptionRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.UserRepository;
import com.matrimony.service.UserSubscriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subscriptions")
@CrossOrigin(allowedHeaders = "*")
public class UserSubscriptionController {

    @Autowired
    private UserSubscriptionService subscriptionService;
    
    @Autowired
    private UserRepository userRepository;

    /* -------------------- CREATE -------------------- */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity createSubscription(@Valid @RequestBody UserSubscriptionRequest request) {
        return subscriptionService.createSubscription(request);
    }

    /* -------------------- CURRENT USER -------------------- */
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMySubscription() {
        return subscriptionService.getCurrentUserSubscription();
    }

    /* -------------------- USER HISTORY -------------------- */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity getUserSubscriptions(@PathVariable Long userId) {
        return subscriptionService.getUserSubscriptions(userId);
    }

    /* -------------------- CANCEL -------------------- */
//    @PutMapping("/{id}/cancel")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity cancelSubscription(@PathVariable Long id) {
//        return subscriptionService.cancelSubscription(id);
//    }

    /* -------------------- EXPIRE SUBSCRIPTIONS (ADMIN/CRON) -------------------- */
    @PostMapping("/expire")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity expireSubscriptions() {
        return subscriptionService.expireSubscriptions();
    }

    /* -------------------- PAGINATION (ADMIN) -------------------- */
    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getAllSubscriptionsPaged(Pageable pageable) {
        return subscriptionService.getAllSubscriptionsPaged(pageable);
    }
    
    @GetMapping("/hasActiveSubscription")
    public ResponseEntity checkActiveSubscription() {
    	return subscriptionService.hasActiveSubscription();
    }
}
