package com.matrimony.model.dto.request;

import com.matrimony.model.enums.MessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MessageRequest {

	@NotNull(message = "Receiver user ID is required")
	private Long receiverId;

	@NotBlank(message = "Message content is required")
	@Size(max = 1000, message = "Message content must be at most 1000 characters")
	private String content;

	private MessageType messageType = MessageType.TEXT;

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	// Getters and Setters
	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	@Override
	public String toString() {
		return "MessageRequest [receiverId=" + receiverId + ", content=" + content + ", messageType=" + messageType
				+ ", token=" + token + "]";
	}
	
}