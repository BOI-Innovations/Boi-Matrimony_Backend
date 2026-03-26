package com.matrimony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.RazorpayPayment;

@Repository
public interface RazorpayPaymentRepository extends JpaRepository<RazorpayPayment, String> {

	Optional<RazorpayPayment> findByRazorpayPaymentId(String razorpayPaymentId);
}
