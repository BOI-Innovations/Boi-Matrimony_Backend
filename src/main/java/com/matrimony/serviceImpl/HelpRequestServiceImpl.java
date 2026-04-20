package com.matrimony.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.HelpRequestRequest;
import com.matrimony.model.dto.response.HelpRequestResponse;
import com.matrimony.model.entity.HelpRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.HelpRequestStatus;
import com.matrimony.repository.HelpRequestRepository;
import com.matrimony.service.HelpRequestService;
import com.matrimony.service.UserService;

@Service
public class HelpRequestServiceImpl implements HelpRequestService {

	@Autowired
	private HelpRequestRepository helpRequestRepository;

	@Autowired
	private UserService userService;

	// ✅ Helper method to map entity to response DTO
	private HelpRequestResponse mapToResponse(HelpRequest helpRequest) {
		HelpRequestResponse response = new HelpRequestResponse();
		response.setId(helpRequest.getId().intValue());
		response.setName(helpRequest.getName());
		response.setEmail(helpRequest.getEmail());
		response.setSubject(helpRequest.getSubject());
		response.setMessage(helpRequest.getMessage());
		response.setStatus(helpRequest.getHelpRequestStatus());
		response.setCreatedAt(helpRequest.getCreatedAt());
		return response;
	}

	@Override
	public ResponseEntity createHelpRequest(HelpRequestRequest request) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.getUserByUsername(username);

			if (user == null) {
				return new ResponseEntity("User not found", 404, null);
			}

			HelpRequest helpRequest = new HelpRequest();
			helpRequest.setUser(user);
			helpRequest.setName(request.getName());
			helpRequest.setEmail(request.getEmail());
			helpRequest.setPhoneNumber(request.getPhoneNumber());
			helpRequest.setSubject(request.getSubject());
			helpRequest.setMessage(request.getMessage());
			helpRequest.setHelpRequestStatus(HelpRequestStatus.PENDING);

			HelpRequest saved = helpRequestRepository.save(helpRequest);
			HelpRequestResponse responseDto = mapToResponse(saved);

			return new ResponseEntity("Help request submitted successfully", 200, responseDto);

		} catch (Exception e) {
			return new ResponseEntity("Error saving help request: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getMyHelpRequests() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.getUserByUsername(username);

			if (user == null) {
				return new ResponseEntity("User not found", 404, null);
			}

			List<HelpRequest> list = helpRequestRepository.findByUser(user);
			List<HelpRequestResponse> responses = list.stream().map(this::mapToResponse).collect(Collectors.toList());

			return new ResponseEntity("Help requests retrieved successfully", 200, responses);

		} catch (Exception e) {
			return new ResponseEntity("Error retrieving help requests: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getHelpRequestById(Long id) {
		try {
			Optional<HelpRequest> helpRequest = helpRequestRepository.findById(id);
			if (helpRequest.isPresent()) {
				HelpRequestResponse responseDto = mapToResponse(helpRequest.get());
				return new ResponseEntity("Help request retrieved successfully", 200, responseDto);
			}
			return new ResponseEntity("Help request not found", 404, null);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching help request: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateStatus(Long id, HelpRequestStatus status) {
		try {
			Optional<HelpRequest> existing = helpRequestRepository.findById(id);
			if (existing.isEmpty()) {
				return new ResponseEntity("Help request not found", 404, null);
			}

			HelpRequest helpRequest = existing.get();
			helpRequest.setHelpRequestStatus(status);
			HelpRequest updated = helpRequestRepository.save(helpRequest);

			HelpRequestResponse responseDto = mapToResponse(updated);
			return new ResponseEntity("Status updated successfully", 200, responseDto);

		} catch (Exception e) {
			return new ResponseEntity("Error updating status: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deleteHelpRequest(Long id) {
		try {
			if (!helpRequestRepository.existsById(id)) {
				return new ResponseEntity("Help request not found", 404, null);
			}

			helpRequestRepository.deleteById(id);
			return new ResponseEntity("Help request deleted successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting help request: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllHelpRequests() {
		ResponseEntity response = new ResponseEntity();
		try {
			List<HelpRequest> helpRequests = helpRequestRepository.findAll();
			List<HelpRequestResponse> responses = helpRequests.stream().map(this::mapToResponse)
					.collect(Collectors.toList());

			response.setMessage("Help requests retrieved successfully");
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(responses);

		} catch (Exception e) {
			response.setMessage("Error retrieving help requests: " + e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return response;
	}

	public ResponseEntity getHelpRequests(String search, int page, int limit, LocalDate fromDate, LocalDate toDate) {
		int pageIndex = page - 1;
		Pageable pageable = PageRequest.of(pageIndex, limit);

		// Convert LocalDate to LocalDateTime for database query
		LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
		LocalDateTime toDateTime = toDate != null ? toDate.atTime(23, 59, 59) : null;

		Page<HelpRequest> helpRequestPage;

		// Apply date range filtering
		if (fromDateTime != null || toDateTime != null) {
			if (search != null && !search.trim().isEmpty()) {
				helpRequestPage = helpRequestRepository.searchHelpRequestsWithDateRange(search, fromDateTime,
						toDateTime, pageable);
			} else {
				helpRequestPage = helpRequestRepository.findHelpRequestsByDateRange(fromDateTime, toDateTime, pageable);
			}
		} else {
			if (search != null && !search.trim().isEmpty()) {
				helpRequestPage = helpRequestRepository.searchHelpRequests(search, pageable);
			} else {
				helpRequestPage = helpRequestRepository.findAll(pageable);
			}
		}

		List<Map<String, Object>> helpRequestList = new ArrayList<>();

		for (HelpRequest helpRequest : helpRequestPage.getContent()) {
			Map<String, Object> requestMap = new LinkedHashMap<>();

			requestMap.put("id", helpRequest.getId());
			requestMap.put("name", helpRequest.getName() != null ? helpRequest.getName() : "Not Provided");
			requestMap.put("email", helpRequest.getEmail() != null ? helpRequest.getEmail() : "Not Provided");
			requestMap.put("subject", helpRequest.getSubject() != null ? helpRequest.getSubject() : "Not Provided");
			requestMap.put("message", helpRequest.getMessage() != null ? helpRequest.getMessage() : "Not Provided");

			requestMap.put("submitted",
					helpRequest.getCreatedAt() != null
							? helpRequest.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
							: null);

			requestMap.put("status",
					helpRequest.getHelpRequestStatus() != null ? helpRequest.getHelpRequestStatus().toString()
							: "PENDING");

			// Add user info if available
			if (helpRequest.getUser() != null) {
				Map<String, Object> userMap = new LinkedHashMap<>();
				userMap.put("id", helpRequest.getUser().getId());
				userMap.put("username", helpRequest.getUser().getUsername());
				userMap.put("email", helpRequest.getUser().getEmail());
				userMap.put("phoneNumber", helpRequest.getUser().getPhoneNumber());
				requestMap.put("user", userMap);
			} else {
				requestMap.put("user", null);
			}

			helpRequestList.add(requestMap);
		}

		Map<String, Object> responsePayload = new LinkedHashMap<>();
		responsePayload.put("data", helpRequestList);
		responsePayload.put("pagination", Map.of("currentPage", page, "totalPages", helpRequestPage.getTotalPages(),
				"totalItems", helpRequestPage.getTotalElements(), "itemsPerPage", limit));

		return new ResponseEntity("Success", 200, responsePayload);
	}
}
