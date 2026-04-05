package com.matrimony.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.CreateReportRequest;
import com.matrimony.model.dto.request.ReportActionRequest;
import com.matrimony.model.dto.response.ReportResponse;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.Report;
import com.matrimony.model.entity.ReportAction;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.ReportStatus;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.repository.ReportActionRepository;
import com.matrimony.repository.ReportRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ReportActionRepository reportActionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Override
	public ResponseEntity createReport(CreateReportRequest request) {
		try {
			Long currentUserId = getCurrentUserId();

			User reportedBy = userRepository.findById(currentUserId)
					.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + currentUserId));

			Profile profile = profileRepository.findById(request.getProfileId()).orElseThrow(
					() -> new IllegalArgumentException("Profile not found with id: " + request.getProfileId()));

			if (profile.getUser().getId().equals(currentUserId)) {
				return new ResponseEntity("You cannot report your own profile", 400, null);
			}

			boolean alreadyReported = reportRepository.findByProfileAndReportedBy(profile, reportedBy).isPresent();

			if (alreadyReported) {
				return new ResponseEntity("You have already reported this profile", 409, null);
			}

			Report report = new Report();
			report.setProfile(profile);
			report.setReportedBy(reportedBy);
			report.setReason(request.getReason());
			report.setDescription(request.getDescription());
			report.setStatus(ReportStatus.PENDING);
			report.setCreatedAt(LocalDateTime.now());

			Report savedReport = reportRepository.save(report);

			return new ResponseEntity("Report created successfully", 201, mapToResponse(savedReport));

		} catch (IllegalArgumentException e) {
			return new ResponseEntity(e.getMessage(), 404, null);
		} catch (Exception e) {
			return new ResponseEntity("Internal server error", 500, null);
		}
	}

	@Override
	public ResponseEntity getReportById(Long reportId) {
		try {

			if (reportId == null) {
				return new ResponseEntity("Report id cannot be null", HttpStatus.BAD_REQUEST.value(), null);
			}
			Report report = reportRepository.findById(reportId)
					.orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));

			return new ResponseEntity("Report retrieved successfully", 200, mapToResponse(report));

		} catch (IllegalArgumentException e) {
			return new ResponseEntity(e.getMessage(), 404, null);
		} catch (Exception e) {
			return new ResponseEntity("Internal server error", 500, null);
		}
	}

	@Override
	public ResponseEntity getAllReports(String search, int page, int limit) {

		try {

			Page<Report> reportsPage = reportRepository.searchReports(search, PageRequest.of(page - 1, limit));

			if (reportsPage.isEmpty()) {
				return new ResponseEntity("No reports found", 404, null);
			}

			List<Map<String, Object>> reports = reportsPage.stream().map(report -> {

				Map<String, Object> reportMap = new HashMap<>();

				Profile profile = report.getProfile();
				User reportedBy = report.getReportedBy();

				String fullName = null;
				Integer age = null;
				String location = null;

				if (profile != null) {

					fullName = profile.getFirstName() + " " + profile.getLastName();
					location = profile.getPlaceOfBirth();

					if (profile.getDateOfBirth() != null) {
						age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
					}
				}

				reportMap.put("reportId", report.getId());
				reportMap.put("profileId", profile != null ? profile.getId() : null);
				reportMap.put("name", fullName);
				reportMap.put("gender", profile != null ? profile.getGender().name() : null);
				reportMap.put("age", age);
				reportMap.put("location", location);

				reportMap.put("reason", report.getReason().name());
				reportMap.put("description", report.getDescription());
				reportMap.put("status", report.getStatus().name());

				String reportedByName = null;

				if (reportedBy != null) {

				    Profile reporterProfile = reportedBy.getProfile();

				    if (reporterProfile != null) {
				        reportedByName = reporterProfile.getFirstName() + " " + reporterProfile.getLastName();
				    } else {
				        reportedByName = reportedBy.getUsername();
				    }
				}

				reportMap.put("reportedBy", reportedByName);

				reportMap.put("createdAt", report.getCreatedAt() != null ? report.getCreatedAt() : LocalDateTime.now());

				return reportMap;

			}).collect(Collectors.toList());

			Map<String, Object> payload = new HashMap<>();

			payload.put("reports", reports);
			payload.put("currentPage", reportsPage.getNumber() + 1);
			payload.put("totalPages", reportsPage.getTotalPages());
			payload.put("totalReports", reportsPage.getTotalElements());

			return new ResponseEntity("Reports retrieved successfully", 200, payload);

		} catch (Exception e) {
			return new ResponseEntity("Internal server error: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getReportsByStatus(String status, int page, int limit) {
		try {
			ReportStatus reportStatus;
			try {
				reportStatus = ReportStatus.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException e) {
				return new ResponseEntity("Invalid status: " + status
						+ ". Valid statuses: PENDING, UNDER_REVIEW, RESOLVED, WARNING_SENT, SUSPENDED, REJECTED", 400,
						null);
			}

			Page<Report> reportsPage = reportRepository.findByStatus(reportStatus, PageRequest.of(page - 1, limit));

			if (reportsPage.isEmpty()) {
				return new ResponseEntity("No reports found with status: " + status, 404, null);
			}

			List<Map<String, Object>> reports = reportsPage.stream().map(report -> {

				Map<String, Object> reportMap = new HashMap<>();

				Profile profile = report.getProfile();
				User reportedBy = report.getReportedBy();

				String fullName = null;
				Integer age = null;
				String location = null;

				if (profile != null) {

					fullName = profile.getFirstName() + " " + profile.getLastName();
					location = profile.getPlaceOfBirth();

					if (profile.getDateOfBirth() != null) {
						age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
					}
				}

				reportMap.put("reportId", report.getId());
				reportMap.put("profileId", profile != null ? profile.getId() : null);
				reportMap.put("name", fullName);
				reportMap.put("gender", profile != null ? profile.getGender().name() : null);
				reportMap.put("age", age);
				reportMap.put("location", location);

				reportMap.put("reason", report.getReason().name());
				reportMap.put("description", report.getDescription());
				reportMap.put("status", report.getStatus().name());

				reportMap.put("reportedBy", reportedBy != null ? reportedBy.getUsername() : null);

				reportMap.put("createdAt", report.getCreatedAt() != null ? report.getCreatedAt() : LocalDateTime.now());

				return reportMap;

			}).collect(Collectors.toList());

			Map<String, Object> payload = new HashMap<>();

			payload.put("reports", reports);
			payload.put("currentPage", reportsPage.getNumber() + 1);
			payload.put("totalPages", reportsPage.getTotalPages());
			payload.put("totalReports", reportsPage.getTotalElements());

			return new ResponseEntity("Reports retrieved successfully", 200, payload);

		} catch (Exception e) {
			return new ResponseEntity("Internal server error: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getReportActions(Long reportId) {
		try {

			Report report = reportRepository.findById(reportId)
					.orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));

			List<ReportAction> actions = reportActionRepository.findByReportOrderByActionTimeDesc(report);

			if (actions.isEmpty()) {
				return new ResponseEntity("No actions taken on this report yet", 404, null);
			}

			return new ResponseEntity("Report actions retrieved successfully", 200, actions);

		} catch (IllegalArgumentException e) {
			return new ResponseEntity(e.getMessage(), 404, null);
		} catch (Exception e) {
			return new ResponseEntity("Internal server error", 500, null);
		}
	}

	@Override
	public ResponseEntity getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int limit) {
		try {

			if (startDate == null || endDate == null) {
				return new ResponseEntity("Start date and end date are required", 400, null);
			}

			if (startDate.isAfter(endDate)) {
				return new ResponseEntity("Start date cannot be after end date", 400, null);
			}

			Page<Report> reportsPage = reportRepository.findByCreatedAtBetween(startDate, endDate,
					PageRequest.of(page - 1, limit));

			if (reportsPage.isEmpty()) {
				return new ResponseEntity("No reports found in the specified date range", 404, null);
			}

			List<ReportResponse> responses = reportsPage.getContent().stream().map(this::mapToResponse).toList();

			return new ResponseEntity("Reports retrieved successfully", 200, responses);

		} catch (Exception e) {
			return new ResponseEntity("Internal server error", 500, null);
		}
	}

	@Override
	public ResponseEntity createReportAction(Long reportId, ReportActionRequest request) {

		try {

			if (reportId == null) {
				return new ResponseEntity("Report id cannot be null", 400, null);
			}

			Long adminId = getCurrentUserId();

			Report report = reportRepository.findById(reportId)
					.orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));

			User admin = userRepository.findById(adminId)
					.orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + adminId));

			ReportAction action = new ReportAction();
			action.setReport(report);
			action.setAdmin(admin);
			action.setAction(request.getAction());
			action.setNotes(request.getNotes());
			action.setActionTime(LocalDateTime.now());

			reportActionRepository.save(action);

			// Update Report Status
			switch (request.getAction()) {

			case WARNING_SENT -> report.setStatus(ReportStatus.WARNING_SENT);

			case PROFILE_SUSPENDED -> report.setStatus(ReportStatus.SUSPENDED);

			case REPORT_RESOLVED -> report.setStatus(ReportStatus.RESOLVED);

			case REPORT_REJECTED -> report.setStatus(ReportStatus.REJECTED);

			default -> report.setStatus(ReportStatus.UNDER_REVIEW);
			}

			reportRepository.save(report);

			// Build Safe Response (Avoid Returning Entity)
			Map<String, Object> response = new HashMap<>();

			response.put("actionId", action.getId());
			response.put("reportId", report.getId());
			response.put("action", action.getAction().name());
			response.put("notes", action.getNotes());
			response.put("admin", admin.getUsername());
			response.put("actionTime", action.getActionTime());

			return new ResponseEntity("Action taken successfully", 200, response);

		} catch (IllegalArgumentException e) {

			return new ResponseEntity(e.getMessage(), 404, null);

		} catch (Exception e) {

			return new ResponseEntity("Internal server error", 500, null);
		}
	}

	@Override
	public Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

	private ReportResponse mapToResponse(Report report) {

		ReportResponse response = new ReportResponse();

		response.setId(report.getId());
		response.setProfileId(report.getProfile().getId());
		response.setProfileName(report.getProfile().getFirstName() + " " + report.getProfile().getLastName());
		response.setReportedById(report.getReportedBy().getId());
		response.setReportedByName(report.getReportedBy().getUsername());
		response.setReason(report.getReason());
		response.setDescription(report.getDescription());
		response.setStatus(report.getStatus());
		response.setCreatedAt(report.getCreatedAt());

		return response;
	}
}