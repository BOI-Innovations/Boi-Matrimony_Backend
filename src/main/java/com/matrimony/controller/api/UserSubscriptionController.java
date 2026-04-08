package com.matrimony.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.UserSubscriptionRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.UserSubscriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subscriptions")
@CrossOrigin(allowedHeaders = "*")
public class UserSubscriptionController {

	@Autowired
	private UserSubscriptionService subscriptionService;

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity createSubscription(@Valid @RequestBody UserSubscriptionRequest request) {
		return subscriptionService.createSubscription(request);
	}
	
	@PutMapping("/deactivateUserSubscription")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity inactivateSubscriptions(@RequestBody List<String> subscriptionIds) {
	    return subscriptionService.inactivateSubscriptions(subscriptionIds);
	}
	
	@PutMapping("/activateUserSubscription")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity activateUserSubscription(@RequestBody List<String> subscriptionIds) {
	    return subscriptionService.activateUserSubscription(subscriptionIds);
	}

	@GetMapping("/my")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getMySubscription() {
		return subscriptionService.getCurrentUserSubscription();
	}

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

	@PostMapping("/expire")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity expireSubscriptions() {
		return subscriptionService.expireSubscriptions();
	}

//	@GetMapping("/getAllSubscription")
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity getAllSubscriptionsPaged(Pageable pageable) {
//		return subscriptionService.getAllSubscriptionsPaged(pageable);
//	}

	@GetMapping("/getAllSubscription")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getAllSubscriptionsPaged(@RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
		return subscriptionService.getAllSubscriptionsPaged(search, page, limit);
	}

	@GetMapping("/getSubscriptionsByDateRange")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getSubscriptionsByDateRange(@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
		return subscriptionService.getSubscriptionsByDateRange(startDate, endDate, page, limit);
	}

	@GetMapping("/getSubscriptionsByStatus")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getSubscriptionsByStatus(@RequestParam String status,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
		return subscriptionService.getSubscriptionsByStatus(status, page, limit);
	}

	@GetMapping("/hasActiveSubscription")
	public ResponseEntity checkActiveSubscription() {
		return subscriptionService.hasActiveSubscription();
	}
}
