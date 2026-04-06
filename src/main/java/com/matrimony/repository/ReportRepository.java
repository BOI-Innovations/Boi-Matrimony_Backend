package com.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.Report;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.ReportStatus;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	Optional<Report> findByProfileAndReportedBy(Profile profile, User reportedBy);

	Page<Report> findByStatus(ReportStatus status, Pageable pageable);

	Page<Report> findByProfileId(Long profileId, Pageable pageable);

	Page<Report> findByReportedById(Long reportedById, Pageable pageable);

	@Query("SELECT r FROM Report r WHERE r.status = :status AND r.createdAt <= :cutoffDate")
	List<Report> findPendingReportsOlderThan(@Param("status") ReportStatus status,
			@Param("cutoffDate") LocalDateTime cutoffDate);

	@Modifying
	@Transactional
	@Query("UPDATE Report r SET r.status = :newStatus WHERE r.id = :reportId")
	int updateReportStatus(@Param("reportId") Long reportId, @Param("newStatus") ReportStatus newStatus);

	long countByStatus(ReportStatus status);

	@Query("SELECT r.status, COUNT(r) FROM Report r GROUP BY r.status")
	List<Object[]> getReportStatistics();

	Page<Report> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, PageRequest of);

	@Query("""
			SELECT r FROM Report r
			JOIN r.profile p
			WHERE
			LOWER(p.firstName) LIKE LOWER(CONCAT('%',:search,'%'))
			OR LOWER(p.lastName) LIKE LOWER(CONCAT('%',:search,'%'))
			OR LOWER(CONCAT(p.firstName,' ',p.lastName)) LIKE LOWER(CONCAT('%',:search,'%'))
			OR LOWER(r.reason) LIKE LOWER(CONCAT('%',:search,'%'))
			""")
	Page<Report> searchReports(@Param("search") String search, Pageable pageable);

	Long countByStatus(String status);
	
	 @Query("SELECT COUNT(r) FROM Report r WHERE " +
	           "(:fromDate IS NULL OR r.createdAt >= :fromDate) AND " +
	           "(:toDate IS NULL OR r.createdAt <= :toDate)")
	    Long countByCreatedAtBetween(@Param("fromDate") LocalDateTime fromDate,
	                                  @Param("toDate") LocalDateTime toDate);
	    
	    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = :status AND " +
	           "(:fromDate IS NULL OR r.createdAt >= :fromDate) AND " +
	           "(:toDate IS NULL OR r.createdAt <= :toDate)")
	    Long countByStatusAndCreatedAtBetween(@Param("status") ReportStatus status,
	                                           @Param("fromDate") LocalDateTime fromDate,
	                                           @Param("toDate") LocalDateTime toDate);
}