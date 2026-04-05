package com.matrimony.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Report;
import com.matrimony.model.entity.ReportAction;

@Repository
public interface ReportActionRepository extends JpaRepository<ReportAction, Long> {

	List<ReportAction> findByReportOrderByActionTimeDesc(Report report);

	Page<ReportAction> findByAdminId(Long adminId, Pageable pageable);

}