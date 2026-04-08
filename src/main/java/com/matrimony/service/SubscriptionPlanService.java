package com.matrimony.service;

import com.matrimony.model.dto.request.SubscriptionPlanRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface SubscriptionPlanService {

	ResponseEntity createSubscriptionPlan(SubscriptionPlanRequest request);

	ResponseEntity getAllSubscriptionPlans(String search, Boolean isActive, int page, int limit);

	ResponseEntity getSubscriptionPlanById(String planId);

	ResponseEntity updateSubscriptionPlan(String planId, SubscriptionPlanRequest request);

	ResponseEntity deleteSubscriptionPlan(String planId);

	ResponseEntity getActiveSubscriptionPlans();

	ResponseEntity getPlanPurchaseStatistics();

	ResponseEntity getActivePlanPurchaseStatistics();

	ResponseEntity getPlanWithMemberCount(String planId);
}