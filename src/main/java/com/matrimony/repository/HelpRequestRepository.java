package com.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.matrimony.model.entity.HelpRequest;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.HelpRequestStatus;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
	List<HelpRequest> findByUser(User user);

	Long countByStatus(HelpRequestStatus status);

	@Query("SELECT COUNT(h) FROM HelpRequest h WHERE " + "(:fromDate IS NULL OR h.createdAt >= :fromDate) AND "
			+ "(:toDate IS NULL OR h.createdAt <= :toDate)")
	Long countByCreatedAtBetween(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

	@Query("SELECT COUNT(h) FROM HelpRequest h WHERE h.status = :status AND "
			+ "(:fromDate IS NULL OR h.createdAt >= :fromDate) AND " + "(:toDate IS NULL OR h.createdAt <= :toDate)")
	Long countByStatusAndCreatedAtBetween(@Param("status") HelpRequestStatus status,
			@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

	@Query("SELECT h FROM HelpRequest h WHERE " +
	           "LOWER(h.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	           "LOWER(h.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	           "LOWER(h.subject) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	           "LOWER(h.message) LIKE LOWER(CONCAT('%', :search, '%'))")
	    Page<HelpRequest> searchHelpRequests(@Param("search") String search, Pageable pageable);
	
	 @Query("SELECT h FROM HelpRequest h WHERE " +
	           "(:fromDateTime IS NULL OR h.createdAt >= :fromDateTime) AND " +
	           "(:toDateTime IS NULL OR h.createdAt <= :toDateTime)")
	    Page<HelpRequest> findHelpRequestsByDateRange(@Param("fromDateTime") LocalDateTime fromDateTime,
	                                                  @Param("toDateTime") LocalDateTime toDateTime,
	                                                  Pageable pageable);
	 
	 @Query("SELECT h FROM HelpRequest h WHERE " +
	           "(:status IS NULL OR h.status = :status) AND " +
	           "(:fromDateTime IS NULL OR h.createdAt >= :fromDateTime) AND " +
	           "(:toDateTime IS NULL OR h.createdAt <= :toDateTime) AND " +
	           "(:search IS NULL OR " +
	            "LOWER(h.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	            "LOWER(h.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	            "LOWER(h.subject) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	            "LOWER(h.message) LIKE LOWER(CONCAT('%', :search, '%')))")
	    Page<HelpRequest> findHelpRequestsWithFilters(@Param("search") String search,
	                                                  @Param("status") HelpRequestStatus status,
	                                                  @Param("fromDateTime") LocalDateTime fromDateTime,
	                                                  @Param("toDateTime") LocalDateTime toDateTime,
	                                                  Pageable pageable);

	    @Query("SELECT h FROM HelpRequest h WHERE " +
	           "(:fromDateTime IS NULL OR h.createdAt >= :fromDateTime) AND " +
	           "(:toDateTime IS NULL OR h.createdAt <= :toDateTime) AND " +
	           "(LOWER(h.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	            "LOWER(h.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	            "LOWER(h.subject) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	            "LOWER(h.message) LIKE LOWER(CONCAT('%', :search, '%')))")
	    Page<HelpRequest> searchHelpRequestsWithDateRange(@Param("search") String search,
	                                                      @Param("fromDateTime") LocalDateTime fromDateTime,
	                                                      @Param("toDateTime") LocalDateTime toDateTime,
	                                                      Pageable pageable);

	    boolean existsByEmailAndStatus(String email, HelpRequestStatus status);
}
