package com.matrimony.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.entity.SubscriptionPlan;
import com.matrimony.model.entity.User;
import com.matrimony.model.entity.UserSubscription;
import com.matrimony.model.enums.SubscriptionStatus;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, String> {

	/* -------------------- Basic Finders -------------------- */

	List<UserSubscription> findByUser(User user);

	List<UserSubscription> findByUserAndStatus(User user, SubscriptionStatus status);

	List<UserSubscription> findByStatus(SubscriptionStatus status);

	List<UserSubscription> findByPlan(SubscriptionPlan plan);

	Optional<UserSubscription> findByPaymentId(String paymentId);

	/* -------------------- Date Based Queries -------------------- */

	List<UserSubscription> findByEndDateBefore(LocalDate date);

	List<UserSubscription> findByEndDateBeforeAndStatus(LocalDate date, SubscriptionStatus status);

	@Query("""
			    SELECT s FROM UserSubscription s
			    WHERE s.endDate BETWEEN :startDate AND :endDate
			""")
	List<UserSubscription> findSubscriptionsExpiringBetween(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("""
			    SELECT s FROM UserSubscription s
			    WHERE s.startDate BETWEEN :startDate AND :endDate
			""")
	List<UserSubscription> findSubscriptionsStartedBetween(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	/* -------------------- Active Subscriptions -------------------- */

	@Query("""
			    SELECT s FROM UserSubscription s
			    WHERE s.user.isActive = true AND s.status = com.matrimony.model.enums.SubscriptionStatus.ACTIVE
			""")
	List<UserSubscription> findActiveSubscriptions();

	@Query("""
			    SELECT COUNT(s) FROM UserSubscription s
			    WHERE s.status = com.matrimony.model.enums.SubscriptionStatus.ACTIVE
			""")
	Long countActiveSubscriptions();

	@Query("""
			    SELECT COUNT(s) FROM UserSubscription s
			    WHERE s.plan = :plan
			      AND s.status = com.matrimony.model.enums.SubscriptionStatus.ACTIVE
			""")
	Long countActiveSubscriptionsByPlan(@Param("plan") SubscriptionPlan plan);

	/* -------------------- Updates -------------------- */

	@Modifying
	@Transactional
	@Query("""
			    UPDATE UserSubscription s
			    SET s.status = :status
			    WHERE s.id = :subscriptionId
			""")
	void updateSubscriptionStatus(@Param("subscriptionId") Long subscriptionId,
			@Param("status") SubscriptionStatus status);

	@Modifying
	@Transactional
	@Query("""
			    UPDATE UserSubscription s
			    SET s.status = com.matrimony.model.enums.SubscriptionStatus.EXPIRED
			    WHERE s.endDate < :currentDate
			      AND s.status = com.matrimony.model.enums.SubscriptionStatus.ACTIVE
			""")
	void expireSubscriptions(@Param("currentDate") LocalDate currentDate);

	/* -------------------- History & Pagination -------------------- */

	@Query("""
			    SELECT s FROM UserSubscription s
			    WHERE s.user.isActive = true
			    ORDER BY s.createdAt DESC
			""")
	Page<UserSubscription> findAllSubscriptions(Pageable pageable);

	@Query("""
			    SELECT s FROM UserSubscription s
			    WHERE s.user = :user
			    ORDER BY s.createdAt DESC
			""")
	List<UserSubscription> findUserSubscriptionHistory(@Param("user") User user);

	List<UserSubscription> findByUserOrderByCreatedAtDesc(User user);

	List<UserSubscription> findByUserAndStatusOrderByEndDateDesc(User user, SubscriptionStatus status);

	Collection<UserSubscription> findByUserIdOrderByCreatedAtDesc(Long userId);

	Optional<UserSubscription> findByUserAndPlanAndStatus(User user, SubscriptionPlan plan, SubscriptionStatus active);

	List<UserSubscription> findByUserIdAndStatusOrderByEndDateDesc(Long userId, SubscriptionStatus active);
	
	@Query("""
		    SELECT CASE WHEN COUNT(us) > 0 THEN true ELSE false END
		    FROM UserSubscription us
		    WHERE us.user.id = :userId
		      AND us.status = :status
		""")
		boolean hasActiveSubscription(Long userId, SubscriptionStatus status);

	  @Query("""
		        SELECT COUNT(s) > 0
		        FROM UserSubscription s
		        WHERE s.user.id = :userId
		        AND s.status = com.matrimony.model.enums.SubscriptionStatus.ACTIVE
		        AND s.endDate >= CURRENT_DATE
		    """)
		    boolean existsActiveSubscription(Long userId);


}
