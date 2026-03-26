package com.matrimony.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import com.matrimony.model.dto.request.MessageRequest;
import com.matrimony.model.dto.response.ConversationUserResponse;
import com.matrimony.model.dto.response.MessageResponse;
import com.matrimony.service.MessageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(allowedHeaders = "*")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest messageRequest) {
		MessageResponse response = messageService.sendMessage(messageRequest);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/conversation/{userId}")
	@PreAuthorize("hasRole('USER')")
	public com.matrimony.model.entity.ResponseEntity getConversation(@PathVariable Long userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		return messageService.getConversation(userId, page, size);

	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public com.matrimony.model.entity.ResponseEntity getMessage(@PathVariable Long id) {
		return messageService.getMessageById(id);

	}

	@PutMapping("/{id}/read")
	@PreAuthorize("hasRole('USER')")
	public com.matrimony.model.entity.ResponseEntity markAsRead(@PathVariable Long id) {
		return messageService.markAsRead(id);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public com.matrimony.model.entity.ResponseEntity deleteMessage(@PathVariable Long id) {
		return messageService.deleteMessage(id);
	}

	@GetMapping("/unread/count")
	@PreAuthorize("hasRole('USER')")
	public com.matrimony.model.entity.ResponseEntity getUnreadMessageCount() {
		return messageService.getUnreadMessageCount();
	}

	@GetMapping("/conversations/users")
	@PreAuthorize("hasRole('USER')")
	public com.matrimony.model.entity.ResponseEntity getConversationUsers() {
		try {
			List<ConversationUserResponse> userDTOs = messageService.getConversationUsers();

			return new com.matrimony.model.entity.ResponseEntity("Conversation users fetched successfully", 200,
					userDTOs);

		} catch (Exception e) {
			e.printStackTrace();
			return new com.matrimony.model.entity.ResponseEntity(
					"An error occurred while fetching conversation users: " + e.getMessage(), 500, null);
		}
	}

	@DeleteMapping("/{id}/me")
	@PreAuthorize("hasRole('USER')")
	public com.matrimony.model.entity.ResponseEntity deleteForMe(@PathVariable Long id) {
		return messageService.deleteMessageForMe(id);
	}

	@DeleteMapping("/{id}/everyone")
	@PreAuthorize("hasRole('USER')")
	public com.matrimony.model.entity.ResponseEntity deleteForEveryone(@PathVariable Long id) {
		return messageService.deleteMessageForEveryone(id);
	}

	@PutMapping("/{id}/edit")
	@PreAuthorize("hasRole('USER')")
	public com.matrimony.model.entity.ResponseEntity editMessage(@PathVariable Long id,
			@RequestParam("content") String newContent) {
		return messageService.editMessage(id, newContent);

	}

}