package com.matrimony.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.matrimony.model.dto.request.MessageRequest;
import com.matrimony.model.dto.response.ConversationUserResponse;
import com.matrimony.model.dto.response.MessageResponse;
import com.matrimony.model.entity.Message;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.MessageType;
import com.matrimony.repository.MessageRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.security.jwt.JwtUtils;
import com.matrimony.service.MessageService;
import com.matrimony.service.UserService;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public MessageResponse sendMessage(MessageRequest messageRequest) {
		String token = messageRequest.getToken();
		if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
			throw new RuntimeException("Missing or invalid token");
		}

		String jwt = token.substring(7);
		if (!jwtUtils.validateJwtToken(jwt)) {
			throw new RuntimeException("Invalid token");
		}

		String username = jwtUtils.getUsernameFromToken(jwt);
		User sender = userService.getUserByUsername(username);

		Optional<User> response = userRepository.findById(messageRequest.getReceiverId());
		User receiver = response
				.orElseThrow(() -> new RuntimeException("User not found with id: " + messageRequest.getReceiverId()));

		if (isBlocked(sender, receiver)) {
			throw new RuntimeException("Cannot send message to blocked user");
		}

		Message message = new Message();
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setContent(messageRequest.getContent());
		message.setSentAt(LocalDateTime.now());
		message.setMessageType(
				messageRequest.getMessageType() != null ? messageRequest.getMessageType() : MessageType.TEXT);

		Message savedMessage = messageRepository.save(message);
		return convertToResponse(savedMessage);
	}

	@Override
	public ResponseEntity getConversation(Long userId, int page, int size) {
	    try {
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        User currentUser = userService.getUserByUsername(username);

	        Optional<User> otherUserOpt = userRepository.findById(userId);
	        if (otherUserOpt.isEmpty()) {
	            return new ResponseEntity("User not found", 404, null);
	        }

	        User otherUser = otherUserOpt.get();
	        Pageable pageable = PageRequest.of(page, size);

	        List<Message> messages = messageRepository.findConversation(currentUser.getId(), otherUser.getId(), pageable)
	                .stream()
	                .filter(m -> !(m.getSender().equals(currentUser) && m.getDeletedBySender())
	                        && !(m.getReceiver().equals(currentUser) && m.getDeletedByReceiver()))
	                .collect(Collectors.toList());

	        // Mark unread messages as read
	        messages.stream()
	                .filter(msg -> msg.getReceiver().equals(currentUser) && !msg.getIsRead())
	                .forEach(msg -> {
	                    msg.setIsRead(true);
	                    msg.setReadAt(LocalDateTime.now());
	                });

	        messageRepository.saveAll(messages);

	        List<MessageResponse> responseList = messages.stream()
	                .map(this::convertToResponse)
	                .collect(Collectors.toList());

	        return new ResponseEntity("Conversation fetched successfully", 200, responseList);
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity("An error occurred while fetching conversation: " + e.getMessage(), 500, null);
	    }
	}

	@Override
	public ResponseEntity getMessageById(Long id) {
	    try {
	        Message message = messageRepository.findById(id).orElse(null);
	        if (message == null) {
	            return new ResponseEntity("Message not found with id: " + id, 404, null);
	        }

	        String username = SecurityContextHolder.getContext().getAuthentication().getName();

	        // Mark as read if the current user is the receiver
	        if (message.getReceiver().getUsername().equals(username) && !message.getIsRead()) {
	            message.setIsRead(true);
	            message.setReadAt(LocalDateTime.now());
	            messageRepository.save(message);
	        }

	        MessageResponse response = convertToResponse(message);
	        return new ResponseEntity("Message fetched successfully", 200, response);
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity("An error occurred while fetching the message: " + e.getMessage(), 500, null);
	    }
	}

	@Override
	public ResponseEntity markAsRead(Long id) {
	    try {
	        Message message = messageRepository.findById(id).orElse(null);
	        if (message == null) {
	            return new ResponseEntity("Message not found with id: " + id, 404, null);
	        }

	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        if (!message.getReceiver().getUsername().equals(username)) {
	            return new ResponseEntity("You can only mark your own messages as read", 403, null);
	        }

	        message.setIsRead(true);
	        message.setReadAt(LocalDateTime.now());
	        messageRepository.save(message);

	        return new ResponseEntity("Message marked as read successfully", 200, null);
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity("An error occurred while marking message as read: " + e.getMessage(), 500, null);
	    }
	}

	@Override
	public ResponseEntity deleteMessage(Long id) {
	    try {
	        Message message = messageRepository.findById(id).orElse(null);
	        if (message == null) {
	            return new ResponseEntity("Message not found with id: " + id, 404, null);
	        }

	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        if (!message.getSender().getUsername().equals(username)) {
	            return new ResponseEntity("You can only delete your own messages", 403, null);
	        }

	        messageRepository.delete(message);
	        return new ResponseEntity("Message deleted successfully", 200, null);
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity("An error occurred while deleting the message: " + e.getMessage(), 500, null);
	    }
	}

	@Override
	public ResponseEntity deleteMessageForMe(Long messageId) {
	    try {
	        Message message = messageRepository.findById(messageId).orElse(null);
	        if (message == null) {
	            return new ResponseEntity("Message not found", 404, null);
	        }

	        String username = SecurityContextHolder.getContext().getAuthentication().getName();

	        if (message.getSender().getUsername().equals(username)) {
	            message.setDeletedBySender(true);
	        } else if (message.getReceiver().getUsername().equals(username)) {
	            message.setDeletedByReceiver(true);
	        } else {
	            return new ResponseEntity("You can only delete your own messages", 403, null);
	        }

	        messageRepository.save(message);
	        return new ResponseEntity("Message deleted for you successfully", 200, null);
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity("An error occurred while deleting the message: " + e.getMessage(), 500, null);
	    }
	}

	@Override
	public ResponseEntity deleteMessageForEveryone(Long messageId) {
	    try {
	        Message message = messageRepository.findById(messageId).orElse(null);
	        if (message == null) {
	            return new ResponseEntity("Message not found", 404, null);
	        }

	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        if (!message.getSender().getUsername().equals(username)) {
	            return new ResponseEntity("Only sender can delete for everyone", 403, null);
	        }

	        message.setDeletedBySender(true);
	        message.setDeletedByReceiver(true);
	        messageRepository.save(message);

	        return new ResponseEntity("Message deleted for everyone successfully", 200, null);
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity("An error occurred while deleting message for everyone: " + e.getMessage(), 500, null);
	    }
	}

	@Override
	public ResponseEntity editMessage(Long messageId, String newContent) {
	    try {
	        Message message = messageRepository.findById(messageId).orElse(null);
	        if (message == null) {
	            return new ResponseEntity("Message not found with id: " + messageId, 404, null);
	        }

	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        if (!message.getSender().getUsername().equals(username)) {
	            return new ResponseEntity("You can only edit your own messages", 403, null);
	        }

	        if (newContent == null || newContent.trim().isEmpty()) {
	            return new ResponseEntity("New content cannot be empty", 400, null);
	        }

	        message.setContent(newContent);
	        message.setEditedAt(LocalDateTime.now());
	        Message updatedMessage = messageRepository.save(message);

	        // Convert to DTO to avoid infinite recursion
	        MessageResponse response = convertToResponse(updatedMessage);

	        return new ResponseEntity("Message updated successfully", 200, response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity("An error occurred while editing the message: " + e.getMessage(), 500, null);
	    }
	}

	@Override
	public ResponseEntity getUnreadMessageCount() {
	    try {
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        User user = userService.getUserByUsername(username);

	        int unreadCount = messageRepository.countByReceiverAndIsReadFalseAndDeletedByReceiverFalse(user);

	        return new ResponseEntity(
	                "Unread message count fetched successfully",
	                200,
	                unreadCount
	        );
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity("An error occurred while fetching unread message count: " + e.getMessage(),
	                500, null);
	    }
	}

	private boolean isBlocked(User user1, User user2) {
		// Implement block checking logic
		return false;
	}

	private MessageResponse convertToResponse(Message message) {
		MessageResponse response = new MessageResponse();
		response.setId(message.getId());
		response.setSenderId(message.getSender().getId());
		response.setSenderName(message.getSender().getUsername());
		response.setReceiverId(message.getReceiver().getId());
		response.setReceiverName(message.getReceiver().getUsername());
		response.setContent(message.getContent());
		response.setMessageType(message.getMessageType());
		response.setSentAt(message.getSentAt());
		response.setRead(message.getIsRead());
		response.setReadAt(message.getReadAt());
		return response;
	}

	@Override
	public List<MessageResponse> getUnreadMessages() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.getUserByUsername(username);
		List<Message> unreadMessages = messageRepository.findUnreadMessages(user);
		return unreadMessages.stream().map(this::convertToResponse).collect(Collectors.toList());
	}

    @Override
    public List<ConversationUserResponse> getConversationUsers() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(username);

        List<User> users = messageRepository.findUserConversations(loggedInUser);

        return users.stream().map(convoUser -> {

            Long unreadCount = messageRepository.countUnreadBetweenUsers(convoUser, loggedInUser);

            return new ConversationUserResponse(
                convoUser.getId(),
                convoUser.getUsername(),
                convoUser.getEmail(),
                convoUser.getProfile().getEducationOccupationDetails().getOccupation(),
                convoUser.getProfile().getId(),
                convoUser.getProfile().getFirstName(),
                convoUser.getProfile().getLastName(),
                unreadCount
            );

        }).collect(Collectors.toList());
    }


	@Override
	public List<MessageResponse> getLatestMessages(int page, int size) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.getUserByUsername(username);
		Pageable pageable = PageRequest.of(page, size);
		return messageRepository.findLatestMessages(user, pageable).stream().map(this::convertToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<MessageResponse> getMessagesBetweenDates(LocalDateTime start, LocalDateTime end) {
		List<Message> messages = messageRepository.findMessagesSentBetween(start, end);
		return messages.stream().map(this::convertToResponse).collect(Collectors.toList());
	}

	@Override
	public void deleteAllMessagesForCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.getUserByUsername(username);
		messageRepository.deleteAllUserMessages(user);
	}

}