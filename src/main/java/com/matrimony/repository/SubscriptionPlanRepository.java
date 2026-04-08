package com.matrimony.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.SubscriptionPlan;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, String> {

	Optional<SubscriptionPlan> findByCode(String code);

	boolean existsByCode(String code);

	List<SubscriptionPlan> findByIsActiveTrue();

	List<SubscriptionPlan> findByIsActiveFalse();

	Optional<SubscriptionPlan> findByCodeAndIsActiveTrue(String code);

	Page<SubscriptionPlan> findByIsActive(Boolean isActive, Pageable pageable);

	@Query("SELECT sp FROM SubscriptionPlan sp WHERE "
			+ "(:search IS NULL OR LOWER(sp.name) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(sp.code) LIKE LOWER(CONCAT('%', :search, '%'))) AND "
			+ "(:isActive IS NULL OR sp.isActive = :isActive)")
	Page<SubscriptionPlan> searchSubscriptionPlans(@Param("search") String search, @Param("isActive") Boolean isActive,
			Pageable pageable);

	boolean existsByCodeAndPlanIdNot(String code, String planId);

	// FIXED: Added @Query annotation to properly return a Long count
	@Query("SELECT COUNT(us) FROM UserSubscription us WHERE us.plan.planId = :planId")
	Long getMemberCountByPlanId(@Param("planId") String planId);

	@Query("SELECT sp.planId as planId, sp.code as code, sp.name as name, "
			+ "sp.price as price, sp.durationDays as durationDays, sp.currency as currency, "
			+ "sp.isActive as isActive, COUNT(us.id) as memberCount " + "FROM SubscriptionPlan sp "
			+ "LEFT JOIN UserSubscription us ON sp.planId = us.plan.planId " + "WHERE sp.isActive = true "
			+ "GROUP BY sp.planId, sp.code, sp.name, sp.price, sp.durationDays, sp.currency, sp.isActive "
			+ "ORDER BY memberCount DESC")
	List<Map<String, Object>> getActivePlanPurchaseStatistics();

	@Query("SELECT sp.planId as planId, sp.code as code, sp.name as name, "
			+ "sp.price as price, sp.durationDays as durationDays, sp.currency as currency, "
			+ "sp.isActive as isActive, COUNT(us.id) as memberCount " + "FROM SubscriptionPlan sp "
			+ "LEFT JOIN UserSubscription us ON sp.planId = us.plan.planId "
			+ "GROUP BY sp.planId, sp.code, sp.name, sp.price, sp.durationDays, sp.currency, sp.isActive "
			+ "ORDER BY memberCount DESC")
	List<Map<String, Object>> getPlanPurchaseStatistics();
}