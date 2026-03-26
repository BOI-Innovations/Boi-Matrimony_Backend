package com.matrimony.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.CommunicationSettingsRequest;
import com.matrimony.model.entity.CommunicationSettings;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.CommunicationSettingsRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.CommunicationSettingsService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CommunicationSettingsServiceImpl implements CommunicationSettingsService {

	@Autowired
	private CommunicationSettingsRepository settingsRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

	@Override
	public ResponseEntity saveSettings(CommunicationSettingsRequest request) {
		try {

			Long userId = getCurrentUserId();
			boolean communicationSettings = settingsRepository.existsByUserId(userId);

			if (communicationSettings) {
				return new ResponseEntity("Settings already exist for this user", 400, null);
			}
			User userRef = entityManager.getReference(User.class, userId);

			CommunicationSettings settings = new CommunicationSettings();
			settings.setUser(userRef);
			settings.setEmailNotifications(request.getEmailNotifications());
			settings.setSmsNotifications(request.getSmsNotifications());
			settings.setMatchAlerts(request.getMatchAlerts());
			settings.setMessageNotifications(request.getMessageNotifications());
			settings.setProfileViewAlerts(request.getProfileViewAlerts());
			settings.setWeeklyDigest(request.getWeeklyDigest());
			settings.setPromotionalEmails(request.getPromotionalEmails());

			CommunicationSettings saved = settingsRepository.save(settings);

			return new ResponseEntity("Settings saved successfully", 200, saved);
		} catch (Exception e) {
			return new ResponseEntity("Error saving settings: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getSettings() {
		try {
			Long userId = getCurrentUserId();
			CommunicationSettings settings = settingsRepository.getCommunicationSettingsByUserId(userId);
			if (settings != null) {
				return new ResponseEntity("Settings retrieved successfully", 200, settings);
			} else {
				return new ResponseEntity("Settings not found", 404, null);
			}
		} catch (Exception e) {
			return new ResponseEntity("Error retrieving settings: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateSettings(CommunicationSettingsRequest request) {
		try {
			Long userId = getCurrentUserId();
			CommunicationSettings settings = settingsRepository.getCommunicationSettingsByUserId(userId);

			User userRef = entityManager.getReference(User.class, userId);
			if (settings == null) {
				settings = new CommunicationSettings();
				settings.setUser(userRef);
				settings.setEmailNotifications(request.getEmailNotifications());
				settings.setSmsNotifications(request.getSmsNotifications());
				settings.setMatchAlerts(request.getMatchAlerts());
				settings.setMessageNotifications(request.getMessageNotifications());
				settings.setProfileViewAlerts(request.getProfileViewAlerts());
				settings.setWeeklyDigest(request.getWeeklyDigest());
				settings.setPromotionalEmails(request.getPromotionalEmails());

				CommunicationSettings saved = settingsRepository.save(settings);

				return new ResponseEntity("Settings created successfully", 201, saved);
			} else {
				settings.setEmailNotifications(request.getEmailNotifications());
				settings.setSmsNotifications(request.getSmsNotifications());
				settings.setMatchAlerts(request.getMatchAlerts());
				settings.setMessageNotifications(request.getMessageNotifications());
				settings.setProfileViewAlerts(request.getProfileViewAlerts());
				settings.setWeeklyDigest(request.getWeeklyDigest());
				settings.setPromotionalEmails(request.getPromotionalEmails());

				CommunicationSettings updated = settingsRepository.save(settings);

				return new ResponseEntity("Settings updated successfully", 200, updated);
			}

		} catch (Exception e) {
			return new ResponseEntity("Error processing settings: " + e.getMessage(), 500, null);
		}
	}

}
