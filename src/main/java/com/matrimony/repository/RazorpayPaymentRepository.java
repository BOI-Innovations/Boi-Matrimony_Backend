package com.matrimony.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.RazorpayPayment;
import com.matrimony.model.enums.PaymentStatus;

@Repository
public interface RazorpayPaymentRepository extends JpaRepository<RazorpayPayment, String> {

	Optional<RazorpayPayment> findByRazorpayPaymentId(String razorpayPaymentId);

	@Query("SELECT COALESCE(SUM(p.amount), 0) FROM RazorpayPayment p WHERE p.status = :status")
	BigDecimal sumSuccessfulPayments(@Param("status") PaymentStatus status);

	@Query("SELECT COUNT(p) FROM RazorpayPayment p WHERE p.status = :status")
	Long countByStatus(@Param("status") PaymentStatus status);
	
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM RazorpayPayment p WHERE p.status = :status AND " +
            "(:fromDate IS NULL OR p.createdAt >= :fromDate) AND " +
            "(:toDate IS NULL OR p.createdAt <= :toDate)")
     BigDecimal sumSuccessfulPaymentsByDateRange(@Param("status") PaymentStatus status,
                                                  @Param("fromDate") LocalDateTime fromDate,
                                                  @Param("toDate") LocalDateTime toDate);

}
