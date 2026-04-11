package com.matrimony.controller.api;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.HelpRequestRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.enums.HelpRequestStatus;
import com.matrimony.service.HelpRequestService;

@RestController
@RequestMapping("/api/help-requests")
@CrossOrigin(allowedHeaders = "*")
public class HelpRequestController {

	@Autowired
	private HelpRequestService helpRequestService;

	@PostMapping("/create")
	public ResponseEntity createHelpRequest(@RequestBody HelpRequestRequest request) {
		return helpRequestService.createHelpRequest(request);
	}

	@GetMapping("/my-requests")
	public ResponseEntity getMyHelpRequests() {
		return helpRequestService.getMyHelpRequests();
	}

	@GetMapping("/getAllHelpRequest")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getAllHelpRequests() {
		return helpRequestService.getAllHelpRequests();
	}

	@GetMapping("getHelpRequestById/{id}")
	public ResponseEntity getHelpRequestById(@PathVariable Long id) {
		return helpRequestService.getHelpRequestById(id);
	}

	@PutMapping("updateHelpRequestStatus/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity updateStatus(@PathVariable Long id, @RequestParam HelpRequestStatus status) {
		return helpRequestService.updateStatus(id, status);
	}

	@DeleteMapping("deleteHelpRequestById/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity deleteHelpRequest(@PathVariable Long id) {
		return helpRequestService.deleteHelpRequest(id);
	}
	
	 @GetMapping("/getHelpRequests")
	 @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity getHelpRequests(
	            @RequestParam(required = false) String search,
	            @RequestParam(defaultValue = "1") int page,
	            @RequestParam(defaultValue = "10") int limit,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
	        
	        try {
	            return helpRequestService.getHelpRequests(search, page, limit, fromDate, toDate);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity("Error fetching help requests: " + e.getMessage(), 500, null);
	        }
	    }
	
}