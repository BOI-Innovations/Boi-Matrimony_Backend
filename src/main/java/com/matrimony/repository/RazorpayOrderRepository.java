package com.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.RazorpayOrder;
import com.matrimony.model.enums.OrderStatus;

@Repository
public interface RazorpayOrderRepository extends JpaRepository<RazorpayOrder, String> {

	Optional<RazorpayOrder> findByRazorpayOrderId(String razorpayOrderId);

	Optional<RazorpayOrder> findTopByUser_IdAndPlan_PlanIdAndStatusInOrderByCreatedAtDesc(Long userId, String planId,
			List<OrderStatus> reusableStatuses);
}
