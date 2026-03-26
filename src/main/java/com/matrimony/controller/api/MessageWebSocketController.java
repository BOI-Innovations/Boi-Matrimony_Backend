package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.matrimony.model.dto.request.MessageRequest;
import com.matrimony.model.dto.response.MessageResponse;
import com.matrimony.security.jwt.JwtUtils;
import com.matrimony.service.MessageService;

@Controller
@CrossOrigin(allowedHeaders = "*")
public class MessageWebSocketController {

	@Autowired
	private JwtUtils jwtUtils;

	private final SimpMessagingTemplate messagingTemplate;
	private final MessageService messageService;

	public MessageWebSocketController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
		this.messagingTemplate = messagingTemplate;
		this.messageService = messageService;
	}

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(@Payload MessageRequest messageRequest) {
		try {
			MessageResponse savedMessage = messageService.sendMessage(messageRequest);
			messagingTemplate.convertAndSend("/topic/user/" + savedMessage.getReceiverId(), savedMessage);

			System.out.println("✅ Message sent from User " + savedMessage.getSenderId() + " to User "
					+ savedMessage.getReceiverId() + " via /topic/user/" + savedMessage.getReceiverId());

		} catch (Exception e) {
			System.err.println("❌ Error sending message: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to send message: " + e.getMessage());
		}
	}

	@MessageMapping("/chat.typing")
	public void handleTypingIndicator(@Payload MessageRequest messageRequest) {
		try {
			String token = messageRequest.getToken();
			if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
				return;
			}

			String jwt = token.substring(7);
			if (!jwtUtils.validateJwtToken(jwt)) {
				return;
			}
			messagingTemplate.convertAndSend("/topic/typing/" + messageRequest.getReceiverId(), messageRequest);

		} catch (Exception e) {
			System.err.println("❌ Error handling typing indicator: " + e.getMessage());
		}
	}
}