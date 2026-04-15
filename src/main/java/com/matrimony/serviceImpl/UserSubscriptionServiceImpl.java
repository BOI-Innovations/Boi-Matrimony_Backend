package com.matrimony.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.dto.request.UserSubscriptionRequest;
import com.matrimony.model.dto.response.UserSubscriptionResponse;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.RazorpayPayment;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.SubscriptionPlan;
import com.matrimony.model.entity.User;
import com.matrimony.model.entity.UserSubscription;
import com.matrimony.model.enums.SubscriptionStatus;
import com.matrimony.repository.SubscriptionPlanRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.repository.UserSubscriptionRepository;
import com.matrimony.service.UserService;
import com.matrimony.service.UserSubscriptionService;

@Service
@Transactional
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

	@Autowired
	private UserSubscriptionRepository subscriptionRepository;

	@Autowired
	private SubscriptionPlanRepository planRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	/* -------------------- CREATE -------------------- */

	@Override
	@Transactional
	public ResponseEntity createSubscription(UserSubscriptionRequest request) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.getUserByUsername(username);

			boolean hasActive = subscriptionRepository.findByUserAndStatus(user, SubscriptionStatus.ACTIVE).stream()
					.findAny().isPresent();

			if (hasActive) {
				return new ResponseEntity("User already has an active subscription", "400", null);
			}

			Optional<SubscriptionPlan> optionalPlan = planRepository.findById(request.getPlanId());
			if (optionalPlan.isEmpty()) {
				return new ResponseEntity("Subscription plan not found", "400", null);
			}

			SubscriptionPlan plan = optionalPlan.get();

			if (!Boolean.TRUE.equals(plan.getIsActive())) {
				return new ResponseEntity("Subscription plan is inactive", "400", null);
			}

			LocalDate startDate = LocalDate.now();
			LocalDate endDate = startDate.plusDays(plan.getDurationDays());

			UserSubscription subscription = new UserSubscription();
			subscription.setUser(user);
			subscription.setPlan(plan);
			subscription.setStartDate(startDate);
			subscription.setEndDate(endDate);
//			subscription.setPaymentId(request.getPaymentId());
			subscription.setStatus(SubscriptionStatus.ACTIVE);
			subscription.setCreatedAt(LocalDateTime.now()); // ✅ important

			UserSubscription saved = subscriptionRepository.save(subscription);

			return new ResponseEntity("Subscription created successfully", "200", toResponse(saved));

		} catch (Exception e) {
			// log for debugging
			e.printStackTrace();
			return new ResponseEntity("Error creating subscription: " + e.getMessage(), "500", null);
		}
	}

	/* -------------------- CURRENT USER -------------------- */

	@Override
	public ResponseEntity getCurrentUserSubscription() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.getUserByUsername(username);

			List<UserSubscription> active = subscriptionRepository.findByUserAndStatusOrderByEndDateDesc(user,
					SubscriptionStatus.ACTIVE);

			if (active.isEmpty()) {
				return new ResponseEntity("No active subscription found", "404", null);
			}

			return new ResponseEntity("Success", "200", toResponse(active.get(0)));

		} catch (Exception e) {
			return new ResponseEntity("Error fetching subscription: " + e.getMessage(), "500", null);
		}
	}

	/* -------------------- USER HISTORY -------------------- */

	@Override
	public ResponseEntity getUserSubscriptions(Long userId) {
		try {
			// Fetch subscriptions directly by userId
			List<UserSubscriptionResponse> responses = subscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId)
					.stream().map(this::toResponse).collect(Collectors.toList());

			return new ResponseEntity("Success", "200", responses);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching subscriptions: " + e.getMessage(), "500", null);
		}
	}

	/* -------------------- CANCEL -------------------- */

//	@Override
//	public ResponseEntity cancelSubscription(Long subscriptionId) {
//		try {
//			UserSubscription subscription = subscriptionRepository.findById(subscriptionId)
//					.orElseThrow(() -> new RuntimeException("Subscription not found"));
//
//			String username = SecurityContextHolder.getContext().getAuthentication().getName();
//			if (!subscription.getUser().getUsername().equals(username)) {
//				return new ResponseEntity("Unauthorized", "403", null);
//			}
//
//			subscription.setStatus(SubscriptionStatus.CANCELLED);
//			subscriptionRepository.save(subscription);
//
//			return new ResponseEntity("Subscription cancelled", "200", toResponse(subscription));
//
//		} catch (Exception e) {
//			return new ResponseEntity("Error cancelling subscription: " + e.getMessage(), "500", null);
//		}
//	}

	/* -------------------- EXPIRE (CRON / ADMIN) -------------------- */

	@Override
	public ResponseEntity expireSubscriptions() {
		try {
			subscriptionRepository.expireSubscriptions(LocalDate.now());
			return new ResponseEntity("Expired subscriptions updated", "200", null);
		} catch (Exception e) {
			return new ResponseEntity("Error expiring subscriptions: " + e.getMessage(), "500", null);
		}
	}

	/* -------------------- PAGED -------------------- */

	@Override
	public ResponseEntity getAllSubscriptionsPaged(Pageable pageable) {
		try {
			Page<UserSubscription> page = subscriptionRepository.findAllSubscriptions(pageable);

			if (page.isEmpty()) {
				return new ResponseEntity("No subscriptions found", "404", null);
			}

			List<UserSubscriptionResponse> responses = page.getContent().stream().map(this::toResponse)
					.collect(Collectors.toList());

			return new ResponseEntity("Success", "200", responses);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching subscriptions: " + e.getMessage(), "500", null);
		}
	}

	@Override
	public ResponseEntity hasActiveSubscription() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = userRepository.findUserIdByUsername(username);
			if (userId == null) {
				return new ResponseEntity("User not found", "404", null);
			}
//			List<UserSubscription> activeSubs = subscriptionRepository.findByUserIdAndStatusOrderByEndDateDesc(userId,
//					SubscriptionStatus.ACTIVE);
			
//			 boolean hasActive = subscriptionRepository.hasActiveSubscription(userId, SubscriptionStatus.ACTIVE);
			 
			 boolean hasActive = true;

//			if (activeSubs.isEmpty()) {
//				return new ResponseEntity("No active subscription found", "404", false);
//			} else {
//				return new ResponseEntity("User has an active subscription", "200", true);
//			}
			 
			 if (hasActive) {
		            return new ResponseEntity("User has an active subscription", "200", true);
		        } else {
		            return new ResponseEntity("No active subscription found", "404", false);
		        }

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error checking subscription: " + e.getMessage(), "500", null);
		}
	}

	@Override
	public boolean hasAnActiveSubscription() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findUserIdByUsername(username);

        if (userId == null) {
            return false;
        }

        return subscriptionRepository.hasActiveSubscription(userId, SubscriptionStatus.ACTIVE);
    }
	/* -------------------- MAPPER -------------------- */

	private UserSubscriptionResponse toResponse(UserSubscription s) {
		UserSubscriptionResponse r = new UserSubscriptionResponse();

		r.setId(s.getId());
		r.setUserId(s.getUser().getId());
		r.setUserName(s.getUser().getUsername());

		r.setPlanId(s.getPlan().getPlanId());
		r.setPlanCode(s.getPlan().getCode());
		r.setPlanName(s.getPlan().getName());
		r.setPlanPrice(s.getPlan().getPrice());
		r.setPlanDurationDays(s.getPlan().getDurationDays());

		r.setStartDate(s.getStartDate());
		r.setEndDate(s.getEndDate());
		r.setStatus(s.getStatus());
//		r.setPaymentId(s.getPaymentId());
		r.setCreatedAt(s.getCreatedAt());

		return r;
	}

	@Override
    public ResponseEntity getAllSubscriptionsPaged(String search, int page, int limit) {
        try {
            Pageable pageable = PageRequest.of(page - 1, limit);
            Page<UserSubscription> subscriptionsPage = subscriptionRepository.findAllSubscriptionsWithSearch(search, pageable);
            
            if (subscriptionsPage.isEmpty()) {
                return new ResponseEntity("No subscriptions found", 404, null);
            }
            
            List<UserSubscriptionResponse> responses = subscriptionsPage.getContent().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("subscriptions", responses);
            payload.put("currentPage", subscriptionsPage.getNumber() + 1);
            payload.put("totalPages", subscriptionsPage.getTotalPages());
            payload.put("totalSubscriptions", subscriptionsPage.getTotalElements());
            
            return new ResponseEntity("Success", 200, payload);
            
        } catch (Exception e) {
            return new ResponseEntity("Error fetching subscriptions: " + e.getMessage(), 500, null);
        }
    }
    
    private UserSubscriptionResponse convertToResponse(UserSubscription subscription) {
        UserSubscriptionResponse response = new UserSubscriptionResponse();
        
        response.setId(subscription.getId());
        response.setStartDate(subscription.getStartDate());
        response.setEndDate(subscription.getEndDate());
        response.setStatus(subscription.getStatus());
        response.setCreatedAt(subscription.getCreatedAt());
        
        User user = subscription.getUser();
        if (user != null) {
            response.setUserId(user.getId());
            response.setUserName(user.getUsername());
            
            Profile profile = user.getProfile();
            if (profile != null) {
                String fullName = profile.getFirstName() + " " + profile.getLastName();
                response.setUserName(fullName);
            }
        }
        
        SubscriptionPlan plan = subscription.getPlan();
        if (plan != null) {
            response.setPlanId(plan.getPlanId());
            response.setPlanCode(plan.getCode()); 
            response.setPlanName(plan.getName());
            response.setPlanPrice(plan.getPrice());
            response.setPlanDurationDays(plan.getDurationDays()); 
        }
        
        RazorpayPayment payment = subscription.getPayment();
        if (payment != null) {
            response.setPaymentId(payment.getId());
        }
        
        return response;
    }

    @Override
    public ResponseEntity getSubscriptionsByDateRange(String startDate, String endDate, int page, int limit) {
        try {
            // Parse dates
            LocalDate fromDate = LocalDate.parse(startDate);
            LocalDate toDate = LocalDate.parse(endDate);
            
            Pageable pageable = PageRequest.of(page - 1, limit);
            Page<UserSubscription> subscriptionsPage = subscriptionRepository
                    .findSubscriptionsByDateRange(fromDate, toDate, pageable);
            
            if (subscriptionsPage.isEmpty()) {
                return new ResponseEntity("No subscriptions found between " + startDate + " and " + endDate, 404, null);
            }
            
            List<UserSubscriptionResponse> responses = subscriptionsPage.getContent().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("subscriptions", responses);
            payload.put("currentPage", subscriptionsPage.getNumber() + 1);
            payload.put("totalPages", subscriptionsPage.getTotalPages());
            payload.put("totalSubscriptions", subscriptionsPage.getTotalElements());
            payload.put("startDate", startDate);
            payload.put("endDate", endDate);
            
            return new ResponseEntity("Success", 200, payload);
            
        } catch (DateTimeParseException e) {
            return new ResponseEntity("Invalid date format. Please use yyyy-MM-dd format", 400, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error fetching subscriptions by date range: " + e.getMessage(), 500, null);
        }
    }
    
    @Override
    public ResponseEntity getSubscriptionsByStatus(String status, int page, int limit) {
        try {
            SubscriptionStatus subscriptionStatus;
            try {
                subscriptionStatus = SubscriptionStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return new ResponseEntity("Invalid status. Valid statuses: ACTIVE, EXPIRED, CANCELLED, PENDING_RENEWAL", 400, null);
            }
            
            Pageable pageable = PageRequest.of(page - 1, limit);
            Page<UserSubscription> subscriptionsPage = subscriptionRepository
                    .findSubscriptionsByStatus(subscriptionStatus.toString(), pageable);
            
            if (subscriptionsPage.isEmpty()) {
                return new ResponseEntity("No subscriptions found with status: " + status, 404, null);
            }
            
            List<UserSubscriptionResponse> responses = subscriptionsPage.getContent().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("subscriptions", responses);
            payload.put("currentPage", subscriptionsPage.getNumber() + 1);
            payload.put("totalPages", subscriptionsPage.getTotalPages());
            payload.put("totalSubscriptions", subscriptionsPage.getTotalElements());
            payload.put("status", status);
            
            return new ResponseEntity("Success", 200, payload);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error fetching subscriptions by status: " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity inactivateSubscriptions(List<String> subscriptionIds) {

        if (subscriptionIds == null || subscriptionIds.isEmpty()) {
            return new ResponseEntity("Subscription IDs list cannot be empty", 400, null);
        }

        List<UserSubscription> subscriptions =
                subscriptionRepository.findAllById(subscriptionIds);

        if (subscriptions.isEmpty()) {
            return new ResponseEntity("No subscriptions found", 400, null);
        }

        int updatedCount = 0;

        for (UserSubscription sub : subscriptions) {
            if (sub.getStatus() == SubscriptionStatus.ACTIVE) {
                sub.setStatus(SubscriptionStatus.EXPIRED);
                updatedCount++;
            }
        }

        subscriptionRepository.saveAll(subscriptions);

        return new ResponseEntity(
                updatedCount + " subscription(s) deactivated successfully",
                200,
                subscriptions
        );
    }
    
    @Override
    @Transactional
    public ResponseEntity activateUserSubscription(List<String> subscriptionIds) {

        if (subscriptionIds == null || subscriptionIds.isEmpty()) {
            return new ResponseEntity("Subscription IDs list cannot be empty", 400, null);
        }

        List<UserSubscription> subscriptions =
                subscriptionRepository.findAllById(subscriptionIds);

        if (subscriptions.isEmpty()) {
            return new ResponseEntity("No subscriptions found", 400, null);
        }

        int updatedCount = 0;

        for (UserSubscription sub : subscriptions) {
            if (sub.getStatus() == SubscriptionStatus.EXPIRED) {
                sub.setStatus(SubscriptionStatus.ACTIVE);
                updatedCount++;
            }
        }

        subscriptionRepository.saveAll(subscriptions);

        return new ResponseEntity(
                updatedCount + " subscription(s) activated successfully",
                200,
                subscriptions
        );
    }
	
}
