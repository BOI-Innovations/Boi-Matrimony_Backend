package com.matrimony.controller.api;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.LoginRequest;
import com.matrimony.model.dto.request.SignupRequest;
import com.matrimony.model.dto.response.JwtResponse;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.security.jwt.JwtUtils;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(allowedHeaders = "*")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileRepository profileRepository;

	@PostMapping("/signin")
	public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

			String accessToken = jwtUtils.generateAccessToken(userPrincipal);
			String refreshToken = jwtUtils.generateRefreshToken(userPrincipal);

			Set<String> roles = userPrincipal.getAuthorities().stream().map(auth -> auth.getAuthority())
					.collect(Collectors.toSet());

			User user = userService.getUserByUsername(userPrincipal.getUsername());
			user.setLastLoginAt(LocalDateTime.now());
			userService.save(user);

			String fullName = profileRepository.findFullNameByUserId(user.getId());
			JwtResponse jwtResponse = new JwtResponse(accessToken, user.getId(), user.getUsername(), user.getEmail(),
					fullName, user.getPhoneNumber(), roles, refreshToken);

			return new ResponseEntity("Login successful", HttpStatus.OK.value(), jwtResponse);

		} catch (Exception e) {
			return new ResponseEntity("Invalid username or password: " + e.getMessage(),
					HttpStatus.UNAUTHORIZED.value(), null);
		}
	}

	@PostMapping("/signup")
	public ResponseEntity registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		return userService.createUser(signUpRequest);
	}

	@PostMapping("/adminSignup")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity adminSignup(@Valid @RequestBody SignupRequest signUpRequest) {
		return userService.adminSignup(signUpRequest);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity refreshToken(@RequestHeader("Authorization") String authorizationHeader) {

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity("Invalid refresh token", HttpStatus.BAD_REQUEST.value(), null);
		}

		String refreshToken = authorizationHeader.substring(7);

		if (!jwtUtils.validateJwtToken(refreshToken)) {
			return new ResponseEntity("Authorization header missing or malformed", HttpStatus.BAD_REQUEST.value(),
					null);
		}

		Long userId = jwtUtils.getUserIdFromToken(refreshToken);

		User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		UserPrincipal userPrincipal = UserPrincipal.create(user);

		String newAccessToken = jwtUtils.generateAccessToken(userPrincipal);
		String newRefreshToken = jwtUtils.generateRefreshToken(userPrincipal);
		Set<String> roles = userPrincipal.getAuthorities().stream().map(auth -> auth.getAuthority())
				.collect(Collectors.toSet());

		String fullName = profileRepository.findFullNameByUserId(user.getId());
		JwtResponse jwtResponse = new JwtResponse(newAccessToken, user.getId(), user.getUsername(), user.getEmail(),
				fullName, user.getPhoneNumber(), roles, newRefreshToken);

		return new ResponseEntity("Token refreshed successfully", HttpStatus.OK.value(), jwtResponse);
	}

}
