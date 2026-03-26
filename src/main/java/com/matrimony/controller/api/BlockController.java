package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.BlockRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.BlockService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/blocks")
@CrossOrigin(allowedHeaders = "*")
public class BlockController {

	@Autowired
	private BlockService blockService;

	@PostMapping("/block")
	public ResponseEntity blockUser(@Valid @RequestBody BlockRequest request) {
		return blockService.blockUser(request);
	}

	@DeleteMapping("/unblock/{blockedUserId}")
	public ResponseEntity unblockUser(@PathVariable Long blockedUserId) {
		return blockService.unblockUser(blockedUserId);
	}

	@GetMapping("/blocked")
	public ResponseEntity getBlockedUsers() {
		return blockService.getBlockedUsers();
	}

	@GetMapping("/blocked-by")
	public ResponseEntity getBlockedByUsers() {
		return blockService.getBlockedByUsers();
	}

	@DeleteMapping("/all")
	public ResponseEntity deleteAllBlocksForUser() {
		return blockService.deleteAllBlocksForUser();
	}

	@GetMapping("/is-blocked/{otherUserId}")
	public ResponseEntity isBlocked(@PathVariable Long otherUserId) {
		return blockService.isBlocked(otherUserId);
	}
}
