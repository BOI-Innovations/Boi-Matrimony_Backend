package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.service.ConnectionService;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

	@Autowired
	private ConnectionService connectionService;

	@Autowired
	private ProfileRepository profileRepository;

	@PostMapping("/send/{profileId}")
	public ResponseEntity send(@PathVariable Long profileId) {
		if (profileId == null) {
			return new ResponseEntity("Profile id cannot be null", HttpStatus.BAD_REQUEST.value(), null);
		}
		return connectionService.sendRequest(profileRepository.findUserIdByProfileId(profileId));
	}

	@PostMapping("/accept/{requestId}")
	public ResponseEntity accept(@PathVariable Long requestId) {
		return connectionService.accept(requestId);
	}

	@PostMapping("/reject/{requestId}")
	public ResponseEntity reject(@PathVariable Long requestId) {
		return connectionService.reject(requestId);
	}
	
	@PostMapping("/withdraw/{requestId}")
	public ResponseEntity withdraw(@PathVariable Long requestId) {
		return connectionService.withdraw(requestId);
	}

	@GetMapping("/myconnection")
	public ResponseEntity myConnections() {
		return connectionService.getMyConnections();
	}

	@GetMapping("/requests")
	public ResponseEntity myRequests() {
		return connectionService.getMyRequests();
	}
	
	@GetMapping("/sentByMe")
	public ResponseEntity mySentRequests() {
	    return connectionService.getMySentRequests();
	}

}
