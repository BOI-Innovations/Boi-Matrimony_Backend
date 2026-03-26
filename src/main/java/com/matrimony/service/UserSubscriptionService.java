package com.matrimony.service;

import org.springframework.data.domain.Pageable;

import com.matrimony.model.dto.request.UserSubscriptionRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface UserSubscriptionService {

    /* -------------------- CREATE -------------------- */

    ResponseEntity createSubscription(UserSubscriptionRequest request);

    /* -------------------- CURRENT USER -------------------- */

    ResponseEntity getCurrentUserSubscription();

    /* -------------------- USER HISTORY -------------------- */

    ResponseEntity getUserSubscriptions(Long userId);

    /* -------------------- CANCEL -------------------- */

//    ResponseEntity cancelSubscription(Long subscriptionId);

    /* -------------------- EXPIRE (CRON / ADMIN) -------------------- */

    ResponseEntity expireSubscriptions();

    /* -------------------- PAGINATION -------------------- */

    ResponseEntity getAllSubscriptionsPaged(Pageable pageable);

	ResponseEntity hasActiveSubscription();

	boolean hasAnActiveSubscription();
}
