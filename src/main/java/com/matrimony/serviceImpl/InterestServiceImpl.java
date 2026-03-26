package com.matrimony.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.dto.request.InterestRequest;
import com.matrimony.model.dto.response.InterestResponse;
import com.matrimony.model.entity.Interest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.InterestStatus;
import com.matrimony.repository.InterestRepository;
import com.matrimony.service.InterestService;
import com.matrimony.service.UserService;

@Service
@Transactional
public class InterestServiceImpl implements InterestService {

	@Autowired
	private InterestRepository interestRepository;

	@Autowired
	private UserService userService;

	@Override
	public InterestResponse sendInterest(InterestRequest interestRequest) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User fromUser = userService.getUserByUsername(username);

		ResponseEntity response = userService.getUserById(interestRequest.getToUserId());
		User toUser = (User) response.getPayload();

		// Check if interest already exists
		if (interestRepository.existsByFromUserAndToUser(fromUser, toUser)) {
			throw new RuntimeException("Interest already sent to this user");
		}

		// Check if users have blocked each other
		if (isBlocked(fromUser, toUser)) {
			throw new RuntimeException("Cannot send interest to blocked user");
		}

		Interest interest = new Interest();
		interest.setFromUser(fromUser);
		interest.setToUser(toUser);
		interest.setMessage(interestRequest.getMessage());
		interest.setSentAt(LocalDateTime.now());
		interest.setStatus(InterestStatus.PENDING);

		Interest savedInterest = interestRepository.save(interest);
		return convertToResponse(savedInterest);
	}

	@Override
	public InterestResponse respondToInterest(Long interestId, String status) {
		Interest interest = interestRepository.findById(interestId)
				.orElseThrow(() -> new RuntimeException("Interest not found with id: " + interestId));

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!interest.getToUser().getUsername().equals(username)) {
			throw new RuntimeException("You can only respond to interests sent to you");
		}

		InterestStatus interestStatus;
		try {
			interestStatus = InterestStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid interest status: " + status);
		}

		interest.setStatus(interestStatus);
		interest.setRespondedAt(LocalDateTime.now());

		Interest updatedInterest = interestRepository.save(interest);
		return convertToResponse(updatedInterest);
	}

	@Override
	public void withdrawInterest(Long interestId) {
		Interest interest = interestRepository.findById(interestId)
				.orElseThrow(() -> new RuntimeException("Interest not found with id: " + interestId));

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!interest.getFromUser().getUsername().equals(username)) {
			throw new RuntimeException("You can only withdraw your own interests");
		}

		if (interest.getStatus() != InterestStatus.PENDING) {
			throw new RuntimeException("Cannot withdraw interest that has already been responded to");
		}

		interestRepository.delete(interest);
	}

	@Override
	public List<InterestResponse> getSentInterests() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.getUserByUsername(username);

		return interestRepository.findByFromUser(user).stream().map(this::convertToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InterestResponse> getReceivedInterests() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.getUserByUsername(username);

		return interestRepository.findByToUser(user).stream().map(this::convertToResponse).collect(Collectors.toList());
	}

	@Override
	public List<InterestResponse> getMutualInterests() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.getUserByUsername(username);

		return interestRepository.findMutualInterests(user.getId()).stream().map(this::convertToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public boolean hasSentInterest(Long fromUserId, Long toUserId) {

		ResponseEntity fromUserResponse = userService.getUserById(fromUserId);
		User fromUser = (User) fromUserResponse.getPayload();

		ResponseEntity toUserResponse = userService.getUserById(toUserId);
		User toUser = (User) toUserResponse.getPayload();

		return interestRepository.existsByFromUserAndToUser(fromUser, toUser);
	}

	private boolean isBlocked(User user1, User user2) {
		// Implement block checking logic
		return false;
	}

	private InterestResponse convertToResponse(Interest interest) {
		InterestResponse response = new InterestResponse();
		response.setId(interest.getId());
		response.setFromUserId(interest.getFromUser().getId());
		response.setFromUserName(interest.getFromUser().getUsername());
		response.setToUserId(interest.getToUser().getId());
		response.setToUserName(interest.getToUser().getUsername());
		response.setStatus(interest.getStatus());
		response.setMessage(interest.getMessage());
		response.setSentAt(interest.getSentAt());
		response.setRespondedAt(interest.getRespondedAt());
		return response;
	}
}