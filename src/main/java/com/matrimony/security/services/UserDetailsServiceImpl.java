package com.matrimony.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.entity.User;
import com.matrimony.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> {
			String message = "User not found with username: " + username;
			return new UsernameNotFoundException(message);
		});

		return validateAndCreateUserPrincipal(user);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> {
			String message = "User not found with id: " + id;
			return new UsernameNotFoundException(message);
		});

		return validateAndCreateUserPrincipal(user);
	}

	@Transactional
	public UserDetails loadUserByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> {
			String message = "User not found with email: " + email;
			return new UsernameNotFoundException(message);
		});

		return validateAndCreateUserPrincipal(user);
	}

	private UserPrincipal validateAndCreateUserPrincipal(User user) {
		// Check if user account is active
		if (!user.getIsActive()) {
			throw new UsernameNotFoundException("User account is deactivated: " + user.getUsername());
		}

		// Check if email is verified (if required)
		// if (!user.getEmailVerified()) {
		// throw new UsernameNotFoundException("Email not verified: " +
		// user.getEmail());
		// }

		return UserPrincipal.create(user);
	}

	@Transactional
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Transactional
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Transactional
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

	@Transactional
	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
	}

	@Transactional
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	}
}