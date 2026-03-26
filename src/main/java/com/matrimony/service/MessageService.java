package com.matrimony.service;

import java.time.LocalDateTime;
import java.util.List;

import com.matrimony.model.dto.request.MessageRequest;
import com.matrimony.model.dto.response.ConversationUserResponse;
import com.matrimony.model.dto.response.MessageResponse;
import com.matrimony.model.entity.ResponseEntity;

public interface MessageService {

	ResponseEntity getConversation(Long userId, int page, int size);

	ResponseEntity getMessageById(Long id);

	ResponseEntity markAsRead(Long id);

	ResponseEntity deleteMessage(Long id);

	ResponseEntity getUnreadMessageCount();

	List<MessageResponse> getUnreadMessages();

	List<ConversationUserResponse> getConversationUsers();

	List<MessageResponse> getLatestMessages(int page, int size);

	List<MessageResponse> getMessagesBetweenDates(LocalDateTime start, LocalDateTime end);

	void deleteAllMessagesForCurrentUser();

	MessageResponse sendMessage(MessageRequest messageRequest);

	ResponseEntity deleteMessageForMe(Long messageId);

	ResponseEntity deleteMessageForEveryone(Long messageId);

	ResponseEntity editMessage(Long id, String content);
}