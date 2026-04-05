package com.matrimony.service;

import java.time.LocalDateTime;

import com.matrimony.model.dto.request.CreateReportRequest;
import com.matrimony.model.dto.request.ReportActionRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface ReportService {

	ResponseEntity createReport(CreateReportRequest request);

	ResponseEntity getReportById(Long reportId);

	ResponseEntity getAllReports(String search, int page, int limit);

	ResponseEntity getReportsByStatus(String status, int page, int limit);

	ResponseEntity getReportActions(Long reportId);

	ResponseEntity getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int limit);

	ResponseEntity createReportAction(Long reportId, ReportActionRequest request);

	Long getCurrentUserId();

}