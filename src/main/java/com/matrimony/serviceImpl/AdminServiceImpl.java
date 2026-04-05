package com.matrimony.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.dto.request.UpdateUserRequest;
import com.matrimony.model.dto.response.ProfileResponse;
import com.matrimony.model.dto.response.UserResponse;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.ProfileVerificationStatus;
import com.matrimony.model.enums.RoleName;
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

			if (userId == null) {
				return new ResponseEntity("User id cannot be null", HttpStatus.BAD_REQUEST.value(), null);
			}
			User user = userRepository.findById(userId).orElse(null);
			if (user == null) {
				return new ResponseEntity("User not found with id: " + userId, 404, null);
			}

			userRepository.deleteById(userId);
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

	@Override
	public ResponseEntity getAllAdmins() {
		try {
			List<User> admins = userRepository.findByRolesContaining(RoleName.ROLE_ADMIN);
			if (admins.isEmpty()) {
				return new ResponseEntity("No admin users found.", HttpStatus.NOT_FOUND.value(), null);
			}
			List<UserResponse> adminList = admins.stream().map(this::convertToUserResponse)
					.collect(Collectors.toList());

			return new ResponseEntity("Admin users fetched successfully.", HttpStatus.OK.value(), adminList);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching admin users: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	private UserResponse convertToUserResponse(User user) {

		UserResponse response = new UserResponse();

		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setPhoneNumber(user.getPhoneNumber());
		response.setIsActive(user.getIsActive());
		response.setCreatedAt(user.getCreatedAt());
		response.setLastLoginAt(user.getLastLoginAt());

		Set<String> roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());

		response.setRoles(roles);

		return response;
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

	@Override
	public ResponseEntity getUsers(String search, int page, int limit) {
		try {
			var usersPage = userRepository.searchUsers(search, PageRequest.of(page - 1, limit));

			if (usersPage.isEmpty()) {
				return new ResponseEntity("No users found", 404, null);
			}

			List<Map<String, Object>> users = usersPage.stream().map(user -> {
				Map<String, Object> userMap = new HashMap<>();
				Profile profile = user.getProfile();

				String fullName = user.getUsername();
				Integer age = null;
				String location = null;

				if (profile != null) {
					fullName = profile.getFirstName() + " " + profile.getLastName();
					location = profile.getPlaceOfBirth();
					if (profile.getDateOfBirth() != null) {
						age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
					}
				}

				userMap.put("id", user.getId());
				userMap.put("name", fullName);
				userMap.put("gender", profile != null ? profile.getGender().name() : null);
				userMap.put("age", age);
				userMap.put("location", location);
				userMap.put("email", user.getEmail());
				userMap.put("phone", user.getPhoneNumber());
				userMap.put("status", user.getIsActive() ? "Active" : "Suspended");
				userMap.put("date", user.getCreatedAt() != null ? user.getCreatedAt() : LocalDateTime.now());

				return userMap;
			}).collect(Collectors.toList());

			Map<String, Object> payload = new HashMap<>();
			payload.put("users", users);
			payload.put("currentPage", usersPage.getNumber() + 1);
			payload.put("totalPages", usersPage.getTotalPages());
			payload.put("totalUsers", usersPage.getTotalElements());

			return new ResponseEntity("Success", 200, payload);

		} catch (Exception e) {
			return new ResponseEntity("Internal server error: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getSuspendedUsers(String search, int page, int limit) {
		try {
			var usersPage = userRepository.searchInactiveUsers(search, PageRequest.of(page - 1, limit));

			if (usersPage.isEmpty()) {
				return new ResponseEntity("No users found", 404, null);
			}

			List<Map<String, Object>> users = usersPage.stream().map(user -> {
				Map<String, Object> userMap = new HashMap<>();
				Profile profile = user.getProfile();

				String fullName = user.getUsername();
				Integer age = null;
				String location = null;

				if (profile != null) {
					fullName = profile.getFirstName() + " " + profile.getLastName();
					location = profile.getPlaceOfBirth();
					if (profile.getDateOfBirth() != null) {
						age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
					}
				}

				userMap.put("id", user.getId());
				userMap.put("name", fullName);
				userMap.put("gender", profile != null ? profile.getGender().name() : null);
				userMap.put("age", age);
				userMap.put("location", location);
				userMap.put("email", user.getEmail());
				userMap.put("phone", user.getPhoneNumber());
				userMap.put("status", user.getIsActive() ? "Active" : "Suspended");
				userMap.put("date", user.getCreatedAt() != null ? user.getCreatedAt() : LocalDateTime.now());

				return userMap;
			}).collect(Collectors.toList());

			Map<String, Object> payload = new HashMap<>();
			payload.put("users", users);
			payload.put("currentPage", usersPage.getNumber() + 1);
			payload.put("totalPages", usersPage.getTotalPages());
			payload.put("totalUsers", usersPage.getTotalElements());

			return new ResponseEntity("Success", 200, payload);

		} catch (Exception e) {
			return new ResponseEntity("Internal server error: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getUsersByDateRange(String startDate, String endDate, int page, int limit) {

		try {

			LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
			LocalDateTime end = LocalDate.parse(endDate).atTime(23, 59, 59);

			var usersPage = userRepository.findUsersByDateRange(start, end, PageRequest.of(page - 1, limit));

			if (usersPage.isEmpty()) {
				return new ResponseEntity("No users found in this date range", 404, null);
			}

			List<Map<String, Object>> users = usersPage.stream().map(user -> {

				Map<String, Object> userMap = new HashMap<>();
				Profile profile = user.getProfile();

				String fullName = user.getUsername();
				Integer age = null;
				String location = null;

				if (profile != null) {
					fullName = profile.getFirstName() + " " + profile.getLastName();
					location = profile.getPlaceOfBirth();

					if (profile.getDateOfBirth() != null) {
						age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
					}
				}

				userMap.put("id", user.getId());
				userMap.put("name", fullName);
				userMap.put("gender", profile != null ? profile.getGender().name() : null);
				userMap.put("age", age);
				userMap.put("location", location);
				userMap.put("email", user.getEmail());
				userMap.put("phone", user.getPhoneNumber());
				userMap.put("status", user.getIsActive() ? "Active" : "Suspended");
				userMap.put("date", user.getCreatedAt());

				return userMap;

			}).collect(Collectors.toList());

			Map<String, Object> payload = new HashMap<>();
			payload.put("users", users);
			payload.put("currentPage", usersPage.getNumber() + 1);
			payload.put("totalPages", usersPage.getTotalPages());
			payload.put("totalUsers", usersPage.getTotalElements());

			return new ResponseEntity("Success", 200, payload);

		} catch (Exception e) {
			return new ResponseEntity("Internal server error: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getUsersByStatus(String status, int page, int limit) {

		try {

			Boolean isActive;

			if ("active".equalsIgnoreCase(status)) {
				isActive = true;
			} else if ("suspended".equalsIgnoreCase(status)) {
				isActive = false;
			} else {
				return new ResponseEntity("Invalid status. Use Active or Suspended", 400, null);
			}

			var usersPage = userRepository.findByIsActive(isActive, PageRequest.of(page - 1, limit));

			if (usersPage.isEmpty()) {
				return new ResponseEntity("No users found with this status", 404, null);
			}

			List<Map<String, Object>> users = usersPage.stream().map(user -> {

				Map<String, Object> userMap = new HashMap<>();
				Profile profile = user.getProfile();

				String fullName = user.getUsername();
				Integer age = null;
				String location = null;

				if (profile != null) {
					fullName = profile.getFirstName() + " " + profile.getLastName();
					location = profile.getPlaceOfBirth();

					if (profile.getDateOfBirth() != null) {
						age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
					}
				}

				userMap.put("id", user.getId());
				userMap.put("name", fullName);
				userMap.put("gender", profile != null ? profile.getGender().name() : null);
				userMap.put("age", age);
				userMap.put("location", location);
				userMap.put("email", user.getEmail());
				userMap.put("phone", user.getPhoneNumber());
				userMap.put("status", user.getIsActive() ? "Active" : "Suspended");
				userMap.put("date", user.getCreatedAt());

				return userMap;

			}).collect(Collectors.toList());

			Map<String, Object> payload = new HashMap<>();
			payload.put("users", users);
			payload.put("currentPage", usersPage.getNumber() + 1);
			payload.put("totalPages", usersPage.getTotalPages());
			payload.put("totalUsers", usersPage.getTotalElements());

			return new ResponseEntity("Success", 200, payload);

		} catch (Exception e) {
			return new ResponseEntity("Internal server error: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateUser(Long userId, UpdateUserRequest request) {
		try {
			if (userId == null) {
				return new ResponseEntity("User id cannot be null", HttpStatus.BAD_REQUEST.value(), null);
			}
			Optional<User> optionalUser = userRepository.findById(userId);

			if (!optionalUser.isPresent()) {
				return new ResponseEntity("User not found", HttpStatus.NOT_FOUND.value(), null);
			}

			User user = optionalUser.get();

			user.setUsername(request.getUsername().trim());
			user.setEmail(request.getEmail().trim());
			user.setPhoneNumber(request.getPhoneNumber());

			if (request.getRoles() != null && !request.getRoles().isEmpty()) {

				Set<RoleName> roles = new HashSet<>();

				for (String roleString : request.getRoles()) {
					try {
						RoleName role = RoleName.valueOf(roleString.toUpperCase());
						roles.add(role);
					} catch (IllegalArgumentException e) {
						return new ResponseEntity("Invalid role: " + roleString, HttpStatus.BAD_REQUEST.value(), null);
					}
				}

				user.setRoles(roles);
			}

			user.setUpdatedAt(LocalDateTime.now());

			User updatedUser = userRepository.save(user);

			UserResponse response = convertToUserResponse(updatedUser);

			return new ResponseEntity("User updated successfully", HttpStatus.OK.value(), response);

		} catch (Exception e) {

			return new ResponseEntity("Error updating user: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

}