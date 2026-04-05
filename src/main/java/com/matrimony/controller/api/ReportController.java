package com.matrimony.controller.api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.CreateReportRequest;
import com.matrimony.model.dto.request.ReportActionRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@PostMapping("/createReport")
	public ResponseEntity createReport(@RequestBody CreateReportRequest request) {
		return reportService.createReport(request);
	}

	@GetMapping("/getReportById/{reportId}")
	public ResponseEntity getReportById(@PathVariable Long reportId) {
		return reportService.getReportById(reportId);
	}

//	@GetMapping("/getAllReports")
//	public ResponseEntity getAllReports(@RequestParam(defaultValue = "1") int page,
//			@RequestParam(defaultValue = "10") int limit) {
//
//		return reportService.getAllReports(page, limit);
//	}
	
	@GetMapping("/getAllReports")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getAllReports(
	        @RequestParam(required = false, defaultValue = "") String search,
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int limit) {

	    return reportService.getAllReports(search, page, limit);
	}
	@GetMapping("/getReportByStatus")
	public ResponseEntity getReportsByStatus(@RequestParam String status, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int limit) {
		return reportService.getReportsByStatus(status, page, limit);
	}

	@GetMapping("/getReportAction/{reportId}/actions")
	public ResponseEntity getReportActions(@PathVariable Long reportId) {
		return reportService.getReportActions(reportId);
	}

	@PostMapping("/{reportId}/action")
	public ResponseEntity createReportAction(@PathVariable Long reportId, @RequestBody ReportActionRequest request) {
		return reportService.createReportAction(reportId, request);
	}

	@GetMapping("/date-range")
	public ResponseEntity getReportsByDateRange(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
		return reportService.getReportsByDateRange(startDate, endDate, page, limit);
	}
}