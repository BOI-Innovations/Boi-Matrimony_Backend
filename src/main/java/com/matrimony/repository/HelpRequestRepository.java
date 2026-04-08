package com.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.matrimony.model.entity.HelpRequest;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.HelpRequestStatus;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
	List<HelpRequest> findByUser(User user);

	Long countByStatus(String status);

	@Query("SELECT COUNT(h) FROM HelpRequest h WHERE " + "(:fromDate IS NULL OR h.createdAt >= :fromDate) AND "
			+ "(:toDate IS NULL OR h.createdAt <= :toDate)")
	Long countByCreatedAtBetween(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

	@Query("SELECT COUNT(h) FROM HelpRequest h WHERE h.status = :status AND "
			+ "(:fromDate IS NULL OR h.createdAt >= :fromDate) AND " + "(:toDate IS NULL OR h.createdAt <= :toDate)")
	Long countByStatusAndCreatedAtBetween(@Param("status") HelpRequestStatus status,
			@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}
