package com.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.SubscriptionPlan;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, String> {

	Optional<SubscriptionPlan> findByCode(String code);

	boolean existsByCode(String code);

	List<SubscriptionPlan> findByIsActiveTrue();

	List<SubscriptionPlan> findByIsActiveFalse();

	Optional<SubscriptionPlan> findByCodeAndIsActiveTrue(String code);
}
