package com.matrimony.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.User;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.repository.UserRepository;

@Component
public class ProfileCleanupScheduler {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Scheduled(cron = "0 0 1 * * ?") // Run daily at 1 AM
	public void cleanupInactiveProfiles() {
		LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(6);

		// Find users who haven't logged in for 6 months
		List<User> inactiveUsers = userRepository.findAll().stream()
				.filter(user -> user.getLastLoginAt() != null && user.getLastLoginAt().isBefore(cutoffDate)).toList();

		for (User user : inactiveUsers) {
			user.setIsActive(false);
			// Optionally archive or delete profiles
		}

		userRepository.saveAll(inactiveUsers);
	}

	@Scheduled(cron = "0 0 2 * * ?") // Run daily at 2 AM
	public void updateProfileCompletionMetrics() {
		List<Profile> profiles = profileRepository.findAll();

		for (Profile profile : profiles) {
			// Recalculate completion percentage
			// This could be implemented with the ProfileCalculator utility
		}

		profileRepository.saveAll(profiles);
	}
}