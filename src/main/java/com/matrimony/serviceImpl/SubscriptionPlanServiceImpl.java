package com.matrimony.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.dto.request.SubscriptionPlanRequest;
import com.matrimony.model.dto.response.SubscriptionPlanResponse;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.SubscriptionPlan;
import com.matrimony.repository.SubscriptionPlanRepository;
import com.matrimony.service.SubscriptionPlanService;

@Service
@Transactional
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

	@Autowired
	private SubscriptionPlanRepository subscriptionPlanRepository;

	@Override
	public ResponseEntity createSubscriptionPlan(SubscriptionPlanRequest request) {
		try {
			if (subscriptionPlanRepository.existsByCode(request.getCode())) {
				return new ResponseEntity("Subscription plan with code " + request.getCode() + " already exists", 409,
						null);
			}

			SubscriptionPlan plan = new SubscriptionPlan();
			plan.setCode(request.getCode());
			plan.setName(request.getName());
			plan.setPrice(request.getPrice());
			plan.setDurationDays(request.getDurationDays());
			plan.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
			plan.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");

			SubscriptionPlan savedPlan = subscriptionPlanRepository.save(plan);

			return new ResponseEntity("Subscription plan created successfully", 201, convertToResponse(savedPlan));

		} catch (Exception e) {
			return new ResponseEntity("Error creating subscription plan: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllSubscriptionPlans(String search, Boolean isActive, int page, int limit) {
		try {
			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
			Page<SubscriptionPlan> plansPage = subscriptionPlanRepository.searchSubscriptionPlans(search, isActive,
					pageable);

			if (plansPage.isEmpty()) {
				return new ResponseEntity("No subscription plans found", 404, null);
			}

			// Get member counts for each plan - wrap in try-catch to prevent transaction
			// rollback
			List<SubscriptionPlanResponse> responses = new ArrayList<>();
			for (SubscriptionPlan plan : plansPage.getContent()) {
				try {
					Long memberCount = subscriptionPlanRepository.getMemberCountByPlanId(plan.getPlanId());
					responses.add(convertToResponseWithUsers(plan, memberCount != null ? memberCount.intValue() : 0));
				} catch (Exception e) {
					// Log error but continue with default value
					System.err
							.println("Error getting member count for plan " + plan.getPlanId() + ": " + e.getMessage());
					responses.add(convertToResponseWithUsers(plan, 0));
				}
			}

			Map<String, Object> payload = new HashMap<>();
			payload.put("subscriptionPlans", responses);
			payload.put("currentPage", plansPage.getNumber() + 1);
			payload.put("totalPages", plansPage.getTotalPages());
			payload.put("totalPlans", plansPage.getTotalElements());

			return new ResponseEntity("Success", 200, payload);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error fetching subscription plans: " + e.getMessage(), 500, null);
		}
	}

//    @Override
//    public ResponseEntity getSubscriptionPlanById(String planId) {
//        try {
//            SubscriptionPlan plan = subscriptionPlanRepository.findById(planId).orElse(null);
//
//            if (plan == null) {
//                return new ResponseEntity("Subscription plan not found with id: " + planId, 404, null);
//            }
//
//            Long memberCount = subscriptionPlanRepository.getMemberCountByPlanId(planId);
//            
//            return new ResponseEntity("Success", 200, convertToResponseWithUsers(plan, memberCount != null ? memberCount.intValue() : 0));
//
//        } catch (Exception e) {
//            return new ResponseEntity("Error fetching subscription plan: " + e.getMessage(), 500, null);
//        }
//    }

	@Override
	public ResponseEntity getSubscriptionPlanById(String planId) {
		try {
			SubscriptionPlan plan = subscriptionPlanRepository.findById(planId).orElse(null);

			if (plan == null) {
				return new ResponseEntity("Subscription plan not found with id: " + planId, 404, null);
			}
			return new ResponseEntity("Success", 200, plan);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching subscription plan: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateSubscriptionPlan(String planId, SubscriptionPlanRequest request) {
		try {
			SubscriptionPlan plan = subscriptionPlanRepository.findById(planId).orElse(null);

			if (plan == null) {
				return new ResponseEntity("Subscription plan not found with id: " + planId, 404, null);
			}

			if (request.getName() != null) {
				plan.setName(request.getName());
			}

			if (request.getPrice() != null) {
				plan.setPrice(request.getPrice());
			}

			if (request.getDurationDays() != null) {
				plan.setDurationDays(request.getDurationDays());
				if (plan.getPlanId() != null) {
					plan.prePersist();
				}
			}

			if (request.getIsActive() != null) {
				plan.setIsActive(request.getIsActive());
			}

			if (request.getCurrency() != null) {
				plan.setCurrency(request.getCurrency());
			}

			SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);

			Long memberCount = subscriptionPlanRepository.getMemberCountByPlanId(planId);

			return new ResponseEntity("Subscription plan updated successfully", 200,
					convertToResponseWithUsers(updatedPlan, memberCount != null ? memberCount.intValue() : 0));

		} catch (Exception e) {
			return new ResponseEntity("Error updating subscription plan: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deleteSubscriptionPlan(String planId) {
		try {
			SubscriptionPlan plan = subscriptionPlanRepository.findById(planId).orElse(null);

			if (plan == null) {
				return new ResponseEntity("Subscription plan not found with id: " + planId, 404, null);
			}

			Long memberCount = subscriptionPlanRepository.getMemberCountByPlanId(planId);
			if (memberCount != null && memberCount > 0) {
				return new ResponseEntity("Cannot deactivate plan with " + memberCount
						+ " active subscribers. Please inform subscribers first.", 409, null);
			}

			plan.setIsActive(false);
			subscriptionPlanRepository.save(plan);

			return new ResponseEntity("Subscription plan deactivated successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting subscription plan: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getActiveSubscriptionPlans() {
		try {
			List<SubscriptionPlan> activePlans = subscriptionPlanRepository.findByIsActiveTrue();

			if (activePlans.isEmpty()) {
				return new ResponseEntity("No active subscription plans found", 404, null);
			}

			List<SubscriptionPlanResponse> responses = new ArrayList<>();
			for (SubscriptionPlan plan : activePlans) {
				try {
					Long memberCount = subscriptionPlanRepository.getMemberCountByPlanId(plan.getPlanId());
					responses.add(convertToResponseWithUsers(plan, memberCount != null ? memberCount.intValue() : 0));
				} catch (Exception e) {
					System.err
							.println("Error getting member count for plan " + plan.getPlanId() + ": " + e.getMessage());
					responses.add(convertToResponseWithUsers(plan, 0));
				}
			}

			Map<String, Object> payload = new HashMap<>();
			payload.put("subscriptionPlans", responses);
			payload.put("totalActivePlans", responses.size());

			int totalActiveSubscribers = responses.stream().mapToInt(SubscriptionPlanResponse::getUsers).sum();
			payload.put("totalActiveSubscribers", totalActiveSubscribers);

			return new ResponseEntity("Success", 200, payload);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching active subscription plans: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getPlanPurchaseStatistics() {
		try {
			List<Map<String, Object>> statistics = subscriptionPlanRepository.getPlanPurchaseStatistics();

			if (statistics == null || statistics.isEmpty()) {
				return new ResponseEntity("No subscription plans found", 404, null);
			}

			List<Map<String, Object>> planStats = new ArrayList<>();
			long totalMembers = 0;

			for (Map<String, Object> stat : statistics) {
				Map<String, Object> planStat = new HashMap<>();
				planStat.put("planId", stat.get("planId"));
				planStat.put("code", stat.get("code"));
				planStat.put("name", stat.get("name"));
				planStat.put("price", stat.get("price"));
				planStat.put("durationDays", stat.get("durationDays"));
				planStat.put("currency", stat.get("currency"));
				planStat.put("isActive", stat.get("isActive"));

				Long memberCount = (Long) stat.get("memberCount");
				if (memberCount == null)
					memberCount = 0L;
				planStat.put("memberCount", memberCount);
				totalMembers += memberCount;

				planStats.add(planStat);
			}

			for (Map<String, Object> planStat : planStats) {
				Long memberCount = (Long) planStat.get("memberCount");
				if (totalMembers > 0) {
					double percentage = (memberCount * 100.0) / totalMembers;
					planStat.put("percentage", String.format("%.1f%%", percentage));
				} else {
					planStat.put("percentage", "0%");
				}
			}

			Map<String, Object> payload = new HashMap<>();
			payload.put("planStatistics", planStats);
			payload.put("totalPlans", planStats.size());
			payload.put("totalMembersWithSubscriptions", totalMembers);

			return new ResponseEntity("Plan purchase statistics retrieved successfully", 200, payload);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error fetching plan statistics: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getActivePlanPurchaseStatistics() {
		try {
			List<Map<String, Object>> statistics = subscriptionPlanRepository.getActivePlanPurchaseStatistics();

			if (statistics == null || statistics.isEmpty()) {
				return new ResponseEntity("No active subscription plans found", 404, null);
			}

			List<Map<String, Object>> planStats = new ArrayList<>();
			long totalActiveMembers = 0;

			for (Map<String, Object> stat : statistics) {
				Map<String, Object> planStat = new HashMap<>();
				planStat.put("planId", stat.get("planId"));
				planStat.put("code", stat.get("code"));
				planStat.put("name", stat.get("name"));
				planStat.put("price", stat.get("price"));
				planStat.put("durationDays", stat.get("durationDays"));
				planStat.put("currency", stat.get("currency"));
				planStat.put("isActive", stat.get("isActive"));

				Long memberCount = (Long) stat.get("memberCount");
				if (memberCount == null)
					memberCount = 0L;
				planStat.put("memberCount", memberCount);
				totalActiveMembers += memberCount;

				planStats.add(planStat);
			}

			for (Map<String, Object> planStat : planStats) {
				Long memberCount = (Long) planStat.get("memberCount");
				if (totalActiveMembers > 0) {
					double percentage = (memberCount * 100.0) / totalActiveMembers;
					planStat.put("percentage", String.format("%.1f%%", percentage));
				} else {
					planStat.put("percentage", "0%");
				}
			}

			Map<String, Object> payload = new HashMap<>();
			payload.put("activePlanStatistics", planStats);
			payload.put("totalActivePlans", planStats.size());
			payload.put("totalActiveSubscribers", totalActiveMembers);

			return new ResponseEntity("Active plan purchase statistics retrieved successfully", 200, payload);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error fetching active plan statistics: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getPlanWithMemberCount(String planId) {
		try {
			SubscriptionPlan plan = subscriptionPlanRepository.findById(planId).orElse(null);

			if (plan == null) {
				return new ResponseEntity("Subscription plan not found with id: " + planId, 404, null);
			}

			Long memberCount = subscriptionPlanRepository.getMemberCountByPlanId(planId);

			Map<String, Object> payload = new HashMap<>();
			payload.put("planId", plan.getPlanId());
			payload.put("code", plan.getCode());
			payload.put("name", plan.getName());
			payload.put("price", plan.getPrice());
			payload.put("durationDays", plan.getDurationDays());
			payload.put("currency", plan.getCurrency());
			payload.put("isActive", plan.getIsActive());
			payload.put("memberCount", memberCount != null ? memberCount : 0L);

			return new ResponseEntity("Success", 200, payload);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching plan with member count: " + e.getMessage(), 500, null);
		}
	}

	private SubscriptionPlanResponse convertToResponse(SubscriptionPlan plan) {
		return new SubscriptionPlanResponse(plan.getPlanId(), plan.getCode(), plan.getName(), plan.getPrice(),
				plan.getDurationDays(), plan.getIsActive(), plan.getCurrency());
	}

	private SubscriptionPlanResponse convertToResponseWithUsers(SubscriptionPlan plan, Integer users) {
		return new SubscriptionPlanResponse(plan.getPlanId(), plan.getCode(), plan.getName(), plan.getPrice(),
				plan.getDurationDays(), plan.getIsActive(), plan.getCurrency(), users);
	}
}