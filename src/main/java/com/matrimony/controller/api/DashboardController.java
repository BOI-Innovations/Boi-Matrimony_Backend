package com.matrimony.controller.api;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(allowedHeaders = "*")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/getDashboardData")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getDashboardData(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
		try {
			Map<String, Object> dashboardData = dashboardService.getDashboardData(fromDate, toDate);
			return new ResponseEntity("Success", HttpStatus.OK.value(), dashboardData);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching dashboard data: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@GetMapping("/getReportsAndAnalysisData")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getReportsAndAnalysisData(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
		try {
			Map<String, Object> dashboardData = dashboardService.getDateRangeDashboardData(fromDate, toDate);
			return new ResponseEntity("Success", HttpStatus.OK.value(), dashboardData);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching dashboard data: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@GetMapping("/getAdvanceAnalysisData")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getOverviewDashboardData(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
		try {
			Map<String, Object> dashboardData = dashboardService.getAdvanceAnalysisData(fromDate, toDate);
			return new ResponseEntity("Success", HttpStatus.OK.value(), dashboardData);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching dashboard data: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

}