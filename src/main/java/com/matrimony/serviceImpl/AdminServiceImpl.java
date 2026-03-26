package com.matrimony.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.dto.response.ProfileResponse;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.ProfileVerificationStatus;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.service.AdminService;
import com.matrimony.service.UserService;
import com.matrimony.util.ProfileCalculator;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileCalculator profileCalculator;

	@Override
	public ResponseEntity getAllProfiles(int page, int size) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Profile> profilePage = profileRepository.findAll(pageable);

			if (profilePage.isEmpty()) {
				return new ResponseEntity("No profiles found.", 404, null);
			}

			List<ProfileResponse> responses = profilePage.getContent().stream().map(this::convertToProfileResponse)
					.collect(Collectors.toList());

			return new ResponseEntity("Profiles fetched successfully.", 200, responses);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching profiles: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getPendingVerificationProfiles() {
		try {
			List<Profile> profiles = profileRepository.findByVerificationStatus(ProfileVerificationStatus.PENDING);

			if (profiles.isEmpty()) {
				return new ResponseEntity("No pending profiles found.", 404, null);
			}

			List<ProfileResponse> responses = profiles.stream().map(this::convertToProfileResponse)
					.collect(Collectors.toList());

			return new ResponseEntity("Pending profiles fetched successfully.", 200, responses);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching pending profiles: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity verifyProfile(Long profileId) {
		try {
			Profile profile = profileRepository.findById(profileId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found with id: " + profileId, 404, null);
			}

			profile.setVerificationStatus(ProfileVerificationStatus.VERIFIED);
			profile.setUpdatedAt(LocalDateTime.now());
			profileRepository.save(profile);

			return new ResponseEntity("Profile verified successfully.", 200, convertToProfileResponse(profile));
		} catch (Exception e) {
			return new ResponseEntity("Error verifying profile: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity rejectProfile(Long profileId, String reason) {
		try {
			Profile profile = profileRepository.findById(profileId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found with id: " + profileId, 404, null);
			}

			profile.setVerificationStatus(ProfileVerificationStatus.REJECTED);
			// optionally save reason
			profile.setUpdatedAt(LocalDateTime.now());
			profileRepository.save(profile);

			return new ResponseEntity("Profile rejected successfully.", 200, convertToProfileResponse(profile));
		} catch (Exception e) {
			return new ResponseEntity("Error rejecting profile: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getDashboardStats() {
		try {
			Map<String, Object> stats = new HashMap<>();

			long totalUsers = userRepository.count();
			long totalProfiles = profileRepository.count();
			long verifiedProfiles = profileRepository.countByVerificationStatus(ProfileVerificationStatus.VERIFIED);
			long pendingVerification = profileRepository.countByVerificationStatus(ProfileVerificationStatus.PENDING);

			stats.put("totalUsers", totalUsers);
			stats.put("totalProfiles", totalProfiles);
			stats.put("verifiedProfiles", verifiedProfiles);
			stats.put("pendingVerification", pendingVerification);
			stats.put("activeUsers", userRepository.countByIsActive(true));
			stats.put("inactiveUsers", userRepository.countByIsActive(false));

			return new ResponseEntity("Dashboard stats fetched successfully.", 200, stats);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching dashboard stats: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deleteUser(Long userId) {
		try {
			ResponseEntity response = userService.getUserById(userId);
			User user = (User) response.getPayload();

			if (user == null) {
				return new ResponseEntity("User not found with id: " + userId, 404, null);
			}

			userRepository.delete(user);

			return new ResponseEntity("User deleted successfully.", 200, null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting user: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deactivateUser(Long userId) {
		return userService.deactivateUser(userId);
	}

	@Override
	public ResponseEntity activateUser(Long userId) {
		return userService.activateUser(userId);
	}

	private ProfileResponse convertToProfileResponse(Profile profile) {
		ProfileResponse response = new ProfileResponse();
		response.setId(profile.getId());
		response.setUserId(profile.getUser().getId());
		response.setUsername(profile.getUser().getUsername());
		response.setEmail(profile.getUser().getEmail());
		response.setFirstName(profile.getFirstName());
		response.setLastName(profile.getLastName());
		response.setGender(profile.getGender());
		response.setProfileCreatedFor(profile.getProfileCreatedFor());
		response.setDateOfBirth(profile.getDateOfBirth());
		response.setTimeOfBirth(profile.getTimeOfBirth());
		response.setPlaceOfBirth(profile.getPlaceOfBirth());
		response.setAge(profileCalculator.calculateAge(profile.getDateOfBirth()));
		response.setReligion(profile.getReligion());
		response.setCaste(profile.getCaste());
		response.setSubCaste(profile.getSubCaste());
		response.setMaritalStatus(profile.getMaritalStatus());
		response.setHeightIn(profile.getHeightIn());
		response.setWeight(profile.getWeight());
		response.setPhysicalStatus(profile.getPhysicalStatus());
		response.setMotherTongue(profile.getMotherTongue());
		response.setLanguagesKnown(profile.getLanguagesKnown());
		response.setGothra(profile.getGothra());
		response.setStar(profile.getStar());
		response.setRashi(profile.getRashi());
		response.setManglik(profile.getManglik());
		response.setAbout(profile.getAbout());
		response.setProfilePictureUrl(profile.getProfilePictureUrl());
		response.setDietaryHabits(profile.getDietaryHabits());
		response.setDrinkingHabits(profile.getDrinkingHabits());
		response.setSmokingHabits(profile.getSmokingHabits());
		response.setVerificationStatus(profile.getVerificationStatus());
		response.setProfileComplete(profile.getProfileComplete());
		response.setProfileCompletionPercentage(profile.getProfileCompletionPercentage());
		response.setCreatedAt(profile.getCreatedAt());
		response.setUpdatedAt(profile.getUpdatedAt());

		return response;
	}

}