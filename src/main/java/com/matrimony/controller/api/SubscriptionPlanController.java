package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.SubscriptionPlanRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.SubscriptionPlanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {

	@Autowired
	private SubscriptionPlanService subscriptionPlanService;

	@PostMapping("/createSubscriptionPlan")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanRequest request) {
		return subscriptionPlanService.createSubscriptionPlan(request);
	}

	@GetMapping("/getAllSubscriptionPlan")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getAllSubscriptionPlans(@RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(required = false) Boolean isActive, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int limit) {
		return subscriptionPlanService.getAllSubscriptionPlans(search, isActive, page, limit);
	}

	@GetMapping("/getSubscriptionPlanById/{planId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getSubscriptionPlanById(@PathVariable String planId) {
		return subscriptionPlanService.getSubscriptionPlanById(planId);
	}

	@PutMapping("/updateSubscriptinPlanById/{planId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity updateSubscriptionPlan(@PathVariable String planId,
			@Valid @RequestBody SubscriptionPlanRequest request) {
		return subscriptionPlanService.updateSubscriptionPlan(planId, request);
	}

	@DeleteMapping("/deleteSubscriptionPlanById/{planId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity deleteSubscriptionPlan(@PathVariable String planId) {
		return subscriptionPlanService.deleteSubscriptionPlan(planId);
	}

	@GetMapping("/getActiveSubscriptionPlan")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getActiveSubscriptionPlans() {
		return subscriptionPlanService.getActiveSubscriptionPlans();
	}
}