package com.matrimony.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.PrivacySettingsRequest;
import com.matrimony.model.entity.PrivacySettings;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.PhotoVisibility;
import com.matrimony.repository.ConnectionRepository;
import com.matrimony.repository.InterestRepository;
import com.matrimony.repository.PrivacySettingsRepository;
import com.matrimony.repository.UserSubscriptionRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.PrivacySettingsService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PrivacySettingsServiceImpl implements PrivacySettingsService {

	@Autowired
	private PrivacySettingsRepository privacySettingsRepository;

	@Autowired
	private InterestRepository interestRepository;

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;

	@Autowired
	private ConnectionRepository connectionRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

	@Override
	public ResponseEntity saveSettings(PrivacySettingsRequest request) {
		try {
			Long userId = getCurrentUserId();
			boolean isPrivacySettingExist = privacySettingsRepository.existsByUserId(userId);
			if (isPrivacySettingExist)
				return new ResponseEntity("Privacy settings already exist", 400, null);
			User userRef = entityManager.getReference(User.class, userId);
			PrivacySettings settings = new PrivacySettings();
			settings.setUser(userRef);
			settings.setProfileVisibility(request.getProfileVisibility());
			settings.setShowPhotosTo(request.getShowPhotosTo());
			settings.setHideProfile(request.getHideProfile());

			PrivacySettings saved = privacySettingsRepository.save(settings);
			return new ResponseEntity("Privacy settings saved successfully", 200, saved);
		} catch (Exception e) {
			return new ResponseEntity("Error saving settings: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getSettings() {
		try {
			Long userId = getCurrentUserId();
			PrivacySettings settings = privacySettingsRepository.getPrivacySettingByUserId(userId);

			if (settings != null)
				return new ResponseEntity("Privacy settings retrieved successfully", 200, settings);
			else
				return new ResponseEntity("Privacy settings not found", 404, null);
		} catch (Exception e) {
			return new ResponseEntity("Error retrieving settings: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateSettings(PrivacySettingsRequest request) {
		try {
			Long userId = getCurrentUserId();
			PrivacySettings settings = privacySettingsRepository.getPrivacySettingByUserId(userId);
			User userRef = entityManager.getReference(User.class, userId);
			if (settings == null) {
				settings = new PrivacySettings();
				settings.setUser(userRef);
				settings.setProfileVisibility(request.getProfileVisibility());
				settings.setShowPhotosTo(request.getShowPhotosTo());
				settings.setHideProfile(request.getHideProfile());

				PrivacySettings saved = privacySettingsRepository.save(settings);

				return new ResponseEntity("Privacy settings created successfully", 201, saved);
			} else {
				settings.setProfileVisibility(request.getProfileVisibility());
				settings.setShowPhotosTo(request.getShowPhotosTo());
				settings.setHideProfile(request.getHideProfile());

				PrivacySettings updated = privacySettingsRepository.save(settings);

				return new ResponseEntity("Privacy settings updated successfully", 200, updated);
			}

		} catch (Exception e) {
			return new ResponseEntity("Error processing privacy settings: " + e.getMessage(), 500, null);
		}
	}

	public boolean canViewPhoto(User viewer, User owner) {

		if (viewer.getId().equals(owner.getId())) {
			return true;
		}

		PrivacySettings privacy = owner.getPrivacySettings();

		if (privacy == null) {
			return true;
		}

		if (privacy.getShowPhotosTo() == null) {
			return true;
		}

		PhotoVisibility visibility = privacy.getShowPhotosTo();

		switch (visibility) {

		case ALL:
			return true;

		case ACCEPTED:
			boolean accepted = connectionRepository.existsAcceptedConnection(viewer.getId(), owner.getId());
			return accepted;

		case PREMIUM:
			boolean premium = userSubscriptionRepository.existsActiveSubscription(viewer.getId());
			return premium;

		case MATCHES:
			boolean match = isMatching(viewer, owner);
			return match;

		default:
			return false;
		}
	}

	private boolean isMatching(User viewer, User owner) {

		Profile viewerProfile = viewer.getProfile();
		Profile ownerProfile = owner.getProfile();

		if (viewerProfile == null || ownerProfile == null) {
			return false;
		}

		boolean religionMatch = viewerProfile.getReligion().equals(ownerProfile.getReligion());
		boolean maritalStatusMatch = viewerProfile.getMaritalStatus().equals(ownerProfile.getMaritalStatus());

		return religionMatch && maritalStatusMatch;
	}
}
