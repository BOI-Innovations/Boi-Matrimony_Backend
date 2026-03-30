package com.matrimony.serviceImpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.matrimony.model.dto.request.SignupRequest;
import com.matrimony.model.dto.response.UserResponse;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.RoleName;
import com.matrimony.repository.UserRepository;
import com.matrimony.service.EmailService;
import com.matrimony.service.UserService;
import com.matrimony.util.EmailValidator;
import com.matrimony.util.TokenGenerator;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final Map<String, String> otpStore = new HashMap<>();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private EmailValidator emailValidator;

	@Autowired
	private EmailService emailService;

	private final Random random = new Random();

	@Override
	public ResponseEntity createUser(SignupRequest signUpRequest) {
		try {
			if (userRepository.existsByEmail(signUpRequest.getEmail().trim())) {
				return new ResponseEntity("Email is already in use.", 400, null);
			}

			User user = new User();
			user.setUsername(signUpRequest.getUsername());
			user.setEmail(signUpRequest.getEmail());
			user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
			user.setPhoneNumber(signUpRequest.getPhoneNumber());

			Set<String> roleStrings = signUpRequest.getRoles();
			Set<RoleName> roles = new HashSet<>();

			if (roleStrings == null || roleStrings.isEmpty()) {
				roles.add(RoleName.ROLE_USER);
			} else {
				for (String roleString : roleStrings) {
					try {
						RoleName role = RoleName.valueOf(roleString.toUpperCase());
						roles.add(role);
					} catch (IllegalArgumentException e) {
						return new ResponseEntity("Role " + roleString + " is not valid.", 400, null);
					}
				}
			}

			user.setRoles(roles);
			user.setCreatedAt(LocalDateTime.now());
			user.setUpdatedAt(LocalDateTime.now());
			user.setVerificationToken(tokenGenerator.generateVerificationToken());

			User savedUser = userRepository.save(user);

			UserResponse response = convertToUserResponse(savedUser);

			return new ResponseEntity("User created successfully.", 201, response);

		} catch (Exception e) {
			return new ResponseEntity("Error occurred while creating user: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity sendWelcomeEmail(String email) {
		try {
			String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
			if (email == null || !email.matches(emailRegex)) {
				return new ResponseEntity("Invalid email format.", 400, null);
			}

			String subject = "Welcome to Brahman Matrimony";
			String htmlContent;
			try {
				htmlContent = loadHtmlTemplate("templates/email/welcome.html");
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity("Could not load email template.", 500, null);
			}

			String message = htmlContent.replace("{{EMAIL}}", email);
			emailService.sendHtmlEmail(email, subject, message);

			return new ResponseEntity("Welcome email sent successfully.", 200, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error sending welcome email: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity sendOTP(String email) {
		try {
			String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
			if (email == null || !email.matches(emailRegex)) {
				return new ResponseEntity("Invalid email format.", 400, null);
			}

			if (!userRepository.existsByEmail(email)) {
				return new ResponseEntity("Email not found.", 404, null);
			}

			int otp = random.nextInt(900000) + 100000;
			String otpString = String.format("%06d", otp);
			System.out.println("OTP is -------->>" + otpString);
			otpStore.put(email, otpString);

			String subject = "Your One-Time Password";
			String htmlContent;
			try {
				htmlContent = loadHtmlTemplate("templates/email/otp.html");
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity("Could not load email template.", 500, null);
			}

			String message = htmlContent.replace("{{OTP}}", otpString);
			// emailService.sendHtmlEmail(email, subject, message);

			return new ResponseEntity("OTP sent successfully.", 200, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error sending OTP: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity sendOTPForSignUp(String email) {
		try {

			String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

			if (email == null || !email.matches(emailRegex)) {
				return new ResponseEntity("Invalid email format.", 400, null);
			}

			if (userRepository.existsByEmail(email)) {
				return new ResponseEntity("Email already registered. Please log in instead.", 400, null);
			}

			int otp = random.nextInt(900000) + 100000;
			String otpString = String.format("%06d", otp);
			System.out.println("OTP IS ------------>>" + otpString);
			otpStore.put(email, otpString);

			String subject = "Your One-Time Password";
			String htmlContent;
			try {
				htmlContent = loadHtmlTemplate("templates/email/otp.html");
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity("Could not load email template.", 500, null);
			}

			String message = htmlContent.replace("{{OTP}}", otpString);
			// emailService.sendHtmlEmail(email, subject, message);

			return new ResponseEntity("OTP sent successfully.", 200, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error sending OTP: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity verifyOTP(String email, int enteredOtp) {
		String storedOtp = otpStore.get(email);

		if (storedOtp == null) {
			return new ResponseEntity("OTP not sent or session expired.", 400, null);
		}

		if (String.valueOf(enteredOtp).equals(storedOtp)) {
			otpStore.remove(email);
			return new ResponseEntity("OTP verified successfully", 200, true);
		} else {
			return new ResponseEntity("Invalid OTP. Please try again.", 400, null);
		}
	}

	@Override
	public ResponseEntity verifyOTPForForgotPassword(String email, int enteredOtp) {
		String storedOtp = otpStore.get(email);

		if (storedOtp == null) {
			return new ResponseEntity("OTP not sent or session expired.", 400, null);
		}

		if (String.valueOf(enteredOtp).equals(storedOtp)) {
			otpStore.remove(email);

			Optional<User> userOptional = userRepository.findByEmail(email);
			if (!userOptional.isPresent()) {
				return new ResponseEntity("User not found with email: " + email, 404, null);
			}

			User user = userOptional.get();
			user.setVerificationToken(tokenGenerator.generatePasswordResetToken());
			userRepository.save(user);

			return new ResponseEntity("OTP verified successfully", 200, user.getVerificationToken());
		} else {
			return new ResponseEntity("Invalid OTP. Please try again.", 400, null);
		}
	}

	private String loadHtmlTemplate(String templatePath) throws IOException {
		ClassPathResource resource = new ClassPathResource(templatePath);
		try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		}
	}

	@Override
	public ResponseEntity getUserById(Long id) {
		try {
			return userRepository.findById(id).map(user -> {
				UserResponse response = convertToUserResponse(user);
				return new ResponseEntity("User found successfully.", 200, response);
			}).orElseGet(() -> new ResponseEntity("User not found with id: " + id, 404, null));
		} catch (Exception e) {
			return new ResponseEntity("Error retrieving user: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deactivateUser(Long userId) {
		try {
			return userRepository.findById(userId).map(user -> {
				user.setIsActive(false);
				User updatedUser = userRepository.save(user);
				UserResponse response = convertToUserResponse(updatedUser);
				return new ResponseEntity("User deactivated successfully", 200, response);
			}).orElseGet(() -> new ResponseEntity("User not found", 404, null));
		} catch (Exception e) {
			return new ResponseEntity("Failed to deactivate user: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity activateUser(Long userId) {
		try {
			return userRepository.findById(userId).map(user -> {
				user.setIsActive(true);
				User updatedUser = userRepository.save(user);
				UserResponse response = convertToUserResponse(updatedUser);
				return new ResponseEntity("User activated successfully", 200, response);
			}).orElseGet(() -> new ResponseEntity("User not found", 404, null));
		} catch (Exception e) {
			return new ResponseEntity("Failed to activate user: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deleteUser(Long userId) {
		try {
			if (!userRepository.existsById(userId)) {
				return new ResponseEntity("User not found", 404, null);
			}

//			userRepository.deleteById(userId);

			return new ResponseEntity("User deleted successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Failed to delete user: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateLastLogin(Long userId) {
		try {
			Optional<User> userOptional = userRepository.findById(userId);
			if (!userOptional.isPresent()) {
				return new ResponseEntity("User not found", 404, null);
			}

			User user = userOptional.get();
			user.setLastLoginAt(LocalDateTime.now());
			userRepository.save(user);

			return new ResponseEntity("Last login updated successfully.", 200, user);
		} catch (Exception e) {
			return new ResponseEntity("Failed to update last login: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity verifyEmail(String token) {
		try {
			Optional<User> userOptional = userRepository.findByVerificationToken(token);
			if (!userOptional.isPresent()) {
				return new ResponseEntity("Invalid verification token", 400, null);
			}

			User user = userOptional.get();
			user.setEmailVerified(true);
			user.setVerificationToken(null);
			userRepository.save(user);

			return new ResponseEntity("Email verified successfully.", 200, user);
		} catch (Exception e) {
			return new ResponseEntity("Error verifying email: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity requestPasswordReset(String email) {
		try {
			Optional<User> userOptional = userRepository.findByEmail(email);
			if (!userOptional.isPresent()) {
				return new ResponseEntity("User not found with email: " + email, 404, null);
			}

			User user = userOptional.get();
			user.setVerificationToken(tokenGenerator.generatePasswordResetToken());
			userRepository.save(user);

			// Simulate email sending here (for now just return success)
			return new ResponseEntity("Password reset requested successfully.", 200, user);
		} catch (Exception e) {
			return new ResponseEntity("Error requesting password reset: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity resetPassword(String token, String newPassword) {
		try {
			Optional<User> userOptional = userRepository.findByVerificationToken(token);
			if (!userOptional.isPresent()) {
				return new ResponseEntity("Invalid reset token", 400, null);
			}

			User user = userOptional.get();
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setVerificationToken(tokenGenerator.generateVerificationToken());
			userRepository.save(user);

			return new ResponseEntity("Password reset successfully.", 200, null);
		} catch (Exception e) {
			return new ResponseEntity("Error resetting password: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity changePassword(String username, String oldPassword, String newPassword) {
		try {
			Optional<User> userOptional = userRepository.findByUsername(username); // or findByUsername()
			if (!userOptional.isPresent()) {
				return new ResponseEntity("User not found", 404, null);
			}

			User user = userOptional.get();

			if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
				return new ResponseEntity("Old password is incorrect", 400, null);
			}

			if (newPassword.length() < 8) {
				return new ResponseEntity("New password must be at least 8 characters long", 400, null);
			}

			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);

			return new ResponseEntity("Password changed successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error changing password: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateCredentials(String oldUsername, String newUsername, String newEmail) {
		try {
			Optional<User> userOptional = userRepository.findByUsernameWithProfile(oldUsername);
			if (!userOptional.isPresent()) {
				return new ResponseEntity("User not found with username: " + oldUsername, 404, null);
			}

			User user = userOptional.get();

			if (newUsername != null && !newUsername.trim().isEmpty()
					&& !newUsername.equalsIgnoreCase(user.getUsername())) {

				if (userRepository.existsByUsername(newUsername.trim())) {
					return new ResponseEntity("Username already taken", 400, null);
				}

				user.setUsername(newUsername.trim());
			}

			if (newEmail != null) {
				newEmail = newEmail.trim();
				if (!newEmail.isEmpty()) {
					if (!emailValidator.isValidEmail(newEmail)) {
						return new ResponseEntity("Invalid email format", 400, null);
					}
					if (!newEmail.equalsIgnoreCase(user.getEmail())) {
						if (userRepository.existsByEmail(newEmail)) {
							return new ResponseEntity("Email already in use", 400, null);
						}
						user.setEmail(newEmail);
					}
				}
			}

			userRepository.save(user);

			return new ResponseEntity("User credentials updated successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error updating credentials: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateCredentials(String oldUsername, String newUsername, String newEmail,
			String newPhoneNumber) {
		try {
			Optional<User> userOptional = userRepository.findByUsernameWithProfile(oldUsername);

			if (!userOptional.isPresent()) {
				return new ResponseEntity("User not found with username: " + oldUsername, 404, null);
			}

			User user = userOptional.get();

			if (newUsername != null) {
				newUsername = newUsername.trim();

				if (!newUsername.isEmpty() && !newUsername.equalsIgnoreCase(user.getUsername())) {

					if (userRepository.existsByUsername(newUsername)) {
						return new ResponseEntity("Username already taken", 400, null);
					}

					user.setUsername(newUsername);
				}
			}

			if (newEmail != null) {
				newEmail = newEmail.trim();

				if (!newEmail.isEmpty()) {

					if (!emailValidator.isValidEmail(newEmail)) {
						return new ResponseEntity("Invalid email format", 400, null);
					}

					if (!newEmail.equalsIgnoreCase(user.getEmail())) {

						if (userRepository.existsByEmail(newEmail)) {
							return new ResponseEntity("Email already in use", 400, null);
						}

						user.setEmail(newEmail);
					}
				}
			}

			if (newPhoneNumber != null) {
				newPhoneNumber = newPhoneNumber.trim();

				if (!newPhoneNumber.isEmpty() && !newPhoneNumber.equals(user.getPhoneNumber())) {
					user.setPhoneNumber(newPhoneNumber);
				}
			}

			userRepository.save(user);

			return new ResponseEntity("User credentials updated successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error updating credentials: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with email: " + email));
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	private UserResponse convertToUserResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());

		// Convert Set<RoleName> to Set<String> for roles
		Set<String> roles = user.getRoles().stream().map(RoleName::name).collect(Collectors.toSet());
		response.setRoles(roles);

		response.setIsActive(user.getIsActive());
		response.setEmailVerified(user.getEmailVerified());
		response.setCreatedAt(user.getCreatedAt());
		response.setLastLoginAt(user.getLastLoginAt());

		return response;
	}

	@Override
	public ResponseEntity getUsersByIsActive(Boolean isActive) {
		try {
			List<User> users = userRepository.findByIsActive(isActive);
			if (users.isEmpty())
				return new ResponseEntity("No users found with isActive=" + isActive, 404, null);
			return new ResponseEntity("Users found.", 200, users);
		} catch (Exception e) {
			return new ResponseEntity("Error: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getUsersByEmailVerified(Boolean emailVerified) {
		try {
			List<User> users = userRepository.findByEmailVerified(emailVerified);
			if (users.isEmpty())
				return new ResponseEntity("No users found with emailVerified=" + emailVerified, 404, null);
			return new ResponseEntity("Users found.", 200, users);
		} catch (Exception e) {
			return new ResponseEntity("Error: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getUsersByRole(RoleName role) {
		try {
			List<User> users = userRepository.findByRolesContaining(role);
			if (users.isEmpty())
				return new ResponseEntity("No users found with role=" + role, 404, null);
			return new ResponseEntity("Users found.", 200, users);
		} catch (Exception e) {
			return new ResponseEntity("Error: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getUsersByIsActive(Boolean isActive, Pageable pageable) {
		try {
			Page<User> usersPage = userRepository.findByIsActive(isActive, pageable);
			if (usersPage.isEmpty()) {
				return new ResponseEntity("No users found.", 404, null);
			}
			return new ResponseEntity("Users found.", 200, usersPage);
		} catch (Exception e) {
			return new ResponseEntity("Error retrieving paginated users: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getInactiveUsersSince(LocalDateTime date) {
		try {
			List<User> users = userRepository.findInactiveUsersSince(date);
			if (users.isEmpty())
				return new ResponseEntity("No inactive users since: " + date, 404, null);
			return new ResponseEntity("Inactive users retrieved successfully.", 200, users);
		} catch (Exception e) {
			return new ResponseEntity("Error retrieving inactive users: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getUsersRegisteredBetween(LocalDateTime startDate, LocalDateTime endDate) {
		try {
			List<User> users = userRepository.findUsersRegisteredBetween(startDate, endDate);
			if (users.isEmpty())
				return new ResponseEntity("No users registered between the given dates.", 404, null);
			return new ResponseEntity("Users registered between dates retrieved successfully.", 200, users);
		} catch (Exception e) {
			return new ResponseEntity("Error retrieving users: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity countActiveUsers() {
		try {
			Long count = userRepository.countActiveUsers();
			if (count == 0)
				return new ResponseEntity("No active users found.", 404, 0);
			return new ResponseEntity("Count of active users retrieved successfully.", 200, count);
		} catch (Exception e) {
			return new ResponseEntity("Error retrieving active user count: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateUserStatus(Long userId, Boolean isActive) {
		try {
			Optional<User> userOptional = userRepository.findById(userId);
			if (!userOptional.isPresent())
				return new ResponseEntity("User not found with id: " + userId, 404, null);

			User user = userOptional.get();
			user.setIsActive(isActive);
			User updatedUser = userRepository.save(user);

			return new ResponseEntity("User status updated successfully.", 200, updatedUser);
		} catch (Exception e) {
			return new ResponseEntity("Failed to update user status: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity searchUsers(String keyword) {
		try {
			List<User> users = userRepository.searchUsers(keyword);
			if (users.isEmpty())
				return new ResponseEntity("No users found matching keyword: " + keyword, 404, null);
			return new ResponseEntity("Users found successfully.", 200, users);
		} catch (Exception e) {
			return new ResponseEntity("Error searching users: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
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
}
