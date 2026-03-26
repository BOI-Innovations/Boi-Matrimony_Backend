package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping("/all-requests")
	public ResponseEntity getAllHelpRequests() {
		return helpRequestService.getAllHelpRequests();
	}

	@GetMapping("/{id}")
	public ResponseEntity getHelpRequestById(@PathVariable Long id) {
		return helpRequestService.getHelpRequestById(id);
	}

	@PutMapping("/{id}/status")
	public ResponseEntity updateStatus(@PathVariable Long id, @RequestParam String status) {
		return helpRequestService.updateStatus(id, status);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteHelpRequest(@PathVariable Long id) {
		return helpRequestService.deleteHelpRequest(id);
	}
}
