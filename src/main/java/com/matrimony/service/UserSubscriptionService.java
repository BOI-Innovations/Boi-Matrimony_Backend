package com.matrimony.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.matrimony.model.dto.request.UserSubscriptionRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface UserSubscriptionService {

	ResponseEntity createSubscription(UserSubscriptionRequest request);

	ResponseEntity getCurrentUserSubscription();

	ResponseEntity getUserSubscriptions(Long userId);

	ResponseEntity expireSubscriptions();

	ResponseEntity getAllSubscriptionsPaged(Pageable pageable);

	ResponseEntity hasActiveSubscription();

	boolean hasAnActiveSubscription();

	ResponseEntity getAllSubscriptionsPaged(String search, int page, int limit);

	ResponseEntity getSubscriptionsByDateRange(String startDate, String endDate, int page, int limit);

	ResponseEntity getSubscriptionsByStatus(String status, int page, int limit);

	ResponseEntity inactivateSubscriptions(List<String> subscriptionIds);

	ResponseEntity activateUserSubscription(List<String> subscriptionIds);
}
