package com.matrimony.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.dto.response.ConnectionResponse;
import com.matrimony.model.entity.Connection;
import com.matrimony.model.entity.Message;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.MessageType;
import com.matrimony.repository.ConnectionRepository;
import com.matrimony.repository.MessageRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.ConnectionService;
import com.matrimony.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class ConnectionServiceImpl implements ConnectionService {

	@Autowired
	private ConnectionRepository connectionRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private MessageRepository messageRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

//	@Override
//	public ResponseEntity sendRequest(Long receiverId) {
//		try {
//			String username = SecurityContextHolder.getContext().getAuthentication().getName();
//			User sender = userService.getUserByUsername(username);
//
//			if (sender.getId().equals(receiverId)) {
//				return new ResponseEntity("Cannot send request to yourself", 400, null);
//			}
//
//			User receiver = userRepo.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));
//
//			Optional<Connection> existing = connectionRepo.findExistingConnection(sender.getId(), receiver.getId());
//
//			if (existing.isPresent()) {
//				return new ResponseEntity("Connection request already exists", 200, toDto(existing.get()));
//			}
//
//			Connection connection = new Connection();
//			connection.setSender(sender);
//			connection.setReceiver(receiver);
//			connection.setStatus(Connection.Status.PENDING);
//
//			Connection saved = connectionRepo.save(connection);
//
//			return new ResponseEntity("Request sent successfully", 201, toDto(saved));
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return new ResponseEntity("Failed to send request: " + ex.getMessage(), 500, null);
//		}
//	}

	@Override
	public ResponseEntity sendRequest(Long receiverId) {
		try {
			Long senderId = getCurrentUserId();

			if (senderId.equals(receiverId)) {
				return new ResponseEntity("Cannot send request to yourself", 400, null);
			}

			Optional<Connection> existing = connectionRepo.findExistingConnection(senderId, receiverId);

			if (existing.isPresent()) {
				return new ResponseEntity("Connection request already exists", 200, toDto(existing.get()));
			}

			User sender = entityManager.getReference(User.class, senderId);
			User receiver = entityManager.getReference(User.class, receiverId);

			Connection connection = new Connection();
			connection.setSender(sender);
			connection.setReceiver(receiver);
			connection.setStatus(Connection.Status.PENDING);

			Connection saved = connectionRepo.save(connection);

			return new ResponseEntity("Request sent successfully", 201, toDto(saved));

		} catch (EntityNotFoundException ex) {
			return new ResponseEntity("Receiver not found", 404, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity("Failed to send request: " + ex.getMessage(), 500, null);
		}
	}

//	@Override
//	public ResponseEntity accept(Long requestId) {
//		try {
//			String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//			User currentUser = userService.getUserByUsername(username);
//
//			Connection connection = connectionRepo.findById(requestId)
//					.orElseThrow(() -> new RuntimeException("Request not found"));
//
//			// Only receiver can accept
//			if (!connection.getReceiver().getId().equals(currentUser.getId())) {
//				return new ResponseEntity("Not authorized to accept this request", 403, null);
//			}
//
//			// Update status
//			connection.setStatus(Connection.Status.ACCEPTED);
//			Connection updated = connectionRepo.save(connection);
//
//			Message message = new Message();
//			message.setSender(currentUser);
//			message.setReceiver(connection.getSender());
//			message.setContent("Thanks for connecting! We can chat now 😊");
//			message.setMessageType(MessageType.TEXT);
//
//			messageRepository.save(message);
//
//			return new ResponseEntity("Request accepted", 200, toDto(updated));
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return new ResponseEntity("Failed to accept request: " + ex.getMessage(), 500, null);
//		}
//	}
	@Override
	public ResponseEntity accept(Long requestId) {
		try {
			Long currentUserId = getCurrentUserId();

			Connection connection = connectionRepo.findById(requestId)
					.orElseThrow(() -> new RuntimeException("Request not found"));

			if (!connection.getReceiver().getId().equals(currentUserId)) {
				return new ResponseEntity("Not authorized to accept this request", 403, null);
			}

			connection.setStatus(Connection.Status.ACCEPTED);
			Connection updated = connectionRepo.save(connection);

			User sender = entityManager.getReference(User.class, connection.getSender().getId());
			User receiver = entityManager.getReference(User.class, currentUserId);

			Message message = new Message();
			message.setSender(receiver);
			message.setReceiver(sender);
			message.setContent("Thanks for connecting! We can chat now 😊");
			message.setMessageType(MessageType.TEXT);

			messageRepository.save(message);

			return new ResponseEntity("Request accepted", 200, toDto(updated));

		} catch (EntityNotFoundException ex) {
			return new ResponseEntity("Request not found", 404, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity("Failed to accept request: " + ex.getMessage(), 500, null);
		}
	}

//	@Override
//	public ResponseEntity reject(Long requestId) {
//		try {
//			String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//			User currentUser = userService.getUserByUsername(username);
//
//			Connection con = connectionRepo.findById(requestId)
//					.orElseThrow(() -> new RuntimeException("Request not found"));
//
//			if (!con.getReceiver().getId().equals(currentUser.getId())) {
//				return new ResponseEntity("Not authorized to reject this request", 403, null);
//			}
//
//			con.setStatus(Connection.Status.REJECTED);
//			Connection updated = connectionRepo.save(con);
//
//			return new ResponseEntity("Request rejected", 200, toDto(updated));
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return new ResponseEntity("Failed to reject request: " + ex.getMessage(), 500, null);
//		}
//	}

	@Override
	public ResponseEntity reject(Long requestId) {
		try {
			Long currentUserId = getCurrentUserId();

			Connection connection = connectionRepo.findById(requestId)
					.orElseThrow(() -> new RuntimeException("Request not found"));

			if (!connection.getReceiver().getId().equals(currentUserId)) {
				return new ResponseEntity("Not authorized to reject this request", 403, null);
			}

			connection.setStatus(Connection.Status.REJECTED);
			Connection updated = connectionRepo.save(connection);

			return new ResponseEntity("Request rejected", 200, toDto(updated));

		} catch (EntityNotFoundException ex) {
			return new ResponseEntity("Request not found", 404, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity("Failed to reject request: " + ex.getMessage(), 500, null);
		}
	}
	
	@Override
	public ResponseEntity withdraw(Long requestId) {
	    try {
	        Long currentUserId = getCurrentUserId();

	        Connection connection = connectionRepo.findById(requestId)
	                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

	        if (!connection.getSender().getId().equals(currentUserId)) {
	            return new ResponseEntity("Not authorized to withdraw this request", 403, null);
	        }

	        connectionRepo.delete(connection);

	        return new ResponseEntity("Request withdrawn successfully", 200, null);

	    } catch (EntityNotFoundException ex) {
	        return new ResponseEntity("Request not found", 404, null);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return new ResponseEntity("Failed to withdraw request: " + ex.getMessage(), 500, null);
	    }
	}



	@Override
	public ResponseEntity getMyRequests() {
		try {
			Long currentUserId = getCurrentUserId();
			List<ConnectionResponse> requests = connectionRepo.findByReceiver_Id(currentUserId).stream()
					.map(this::toDto).toList();

			return new ResponseEntity("Fetched received requests", 200, requests);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity("Failed to fetch requests: " + ex.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getMyConnections() {
		try {
			Long currentUserId = getCurrentUserId();

			List<ConnectionResponse> connections = connectionRepo.findAll().stream().filter(
					c -> c.getStatus() == Connection.Status.ACCEPTED && (c.getSender().getId().equals(currentUserId)
							|| c.getReceiver().getId().equals(currentUserId)))
					.map(this::toDto).toList();

			return new ResponseEntity("Fetched connections", 200, connections);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity("Failed to fetch connections: " + ex.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getMySentRequests() {
		try {
			Long userId = getCurrentUserId();
			List<Connection> connections = connectionRepo.findUserConnections(userId);

			List<ConnectionResponse> sent = connections.stream().filter(c -> c.getSender().getId().equals(userId))
					.map(this::toDto).toList();

			List<ConnectionResponse> received = connections.stream().filter(c -> c.getReceiver().getId().equals(userId))
					.map(this::toDto).toList();

			Map<String, Object> response = new HashMap<>();
			response.put("sent", sent);
			response.put("received", received);

			return new ResponseEntity("Fetched user connection requests", 200, response);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity("Failed to fetch requests: " + ex.getMessage(), 500, null);
		}
	}

	private ConnectionResponse toDto(Connection con) {
		ConnectionResponse dto = new ConnectionResponse();
		dto.setId(con.getId());
		dto.setSenderId(profileRepository.findProfileIdByUserId(con.getSender().getId()));
		dto.setReceiverId(profileRepository.findProfileIdByUserId(con.getReceiver().getId()));
		dto.setStatus(con.getStatus().name());
		dto.setCreatedAt(con.getCreatedAt());
		return dto;
	}
}
