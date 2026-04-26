package com.matrimony.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.dto.request.ProfilePreferenceRequest;
import com.matrimony.model.dto.response.ProfilePreferenceResponse;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ProfilePreference;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.ProfilePreferenceRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.ProfilePreferenceService;
import com.matrimony.service.ProfileService;

@Service
public class ProfilePreferenceServiceImpl implements ProfilePreferenceService {

	@Autowired
	private ProfilePreferenceRepository preferenceRepository;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private ProfileRepository profileRepository;

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

	private ProfilePreferenceResponse mapToResponse(ProfilePreference pref) {
		ProfilePreferenceResponse response = new ProfilePreferenceResponse();

		response.setId(pref.getId());
		response.setMinAge(pref.getMinAge());
		response.setMaxAge(pref.getMaxAge());
		response.setMinHeight(pref.getMinHeight());
		response.setMaxHeight(pref.getMaxHeight());
		response.setGender(pref.getGender());
		response.setMaritalStatus(pref.getMaritalStatus());
		response.setHaveChildren(pref.getHaveChildren());
		response.setPhysicalStatus(pref.getPhysicalStatus());

		response.setMotherTongues(pref.getMotherTongues());
		response.setReligion(pref.getReligion());
		response.setCastes(pref.getCastes());
		response.setSubCastes(pref.getSubCastes());
		response.setGothras(pref.getGothras());
		response.setStars(pref.getStars());
		response.setRashis(pref.getRashis());
		response.setManglik(pref.getManglik());
		response.setEducationType(pref.getEducationType());
		response.setEducationLevels(pref.getEducationLevels());
		response.setEmployedIn(pref.getEmployedIn());
		response.setOccupations(pref.getOccupations());
		response.setAnnualIncome(pref.getAnnualIncome());
		response.setDietaryHabits(pref.getDietaryHabits());
		response.setSmokingHabits(pref.getSmokingHabits());
		response.setDrinkingHabits(pref.getDrinkingHabits());
		response.setCitizenships(pref.getCitizenships());
		response.setCountriesLivedIn(pref.getCountriesLivedIn());
		response.setAboutMyPartner(pref.getAboutMyPartner());

		return response;
	}

	private void updateFromRequest(ProfilePreference pref, ProfilePreferenceRequest req) {
		Optional.ofNullable(req.getMinAge()).ifPresent(pref::setMinAge);
		Optional.ofNullable(req.getMaxAge()).ifPresent(pref::setMaxAge);
		Optional.ofNullable(req.getMinHeight()).ifPresent(pref::setMinHeight);
		Optional.ofNullable(req.getMaxHeight()).ifPresent(pref::setMaxHeight);

		Optional.ofNullable(req.getGender()).ifPresent(pref::setGender);
		Optional.ofNullable(req.getMaritalStatus()).ifPresent(pref::setMaritalStatus);
		Optional.ofNullable(req.getHaveChildren()).ifPresent(pref::setHaveChildren);
		Optional.ofNullable(req.getPhysicalStatus()).ifPresent(pref::setPhysicalStatus);

		Optional.ofNullable(req.getMotherTongues()).ifPresent(pref::setMotherTongues);
		Optional.ofNullable(req.getReligion()).ifPresent(pref::setReligion);
		Optional.ofNullable(req.getCastes()).ifPresent(pref::setCastes);
		Optional.ofNullable(req.getSubCastes()).ifPresent(pref::setSubCastes);
		Optional.ofNullable(req.getGothras()).ifPresent(pref::setGothras);
		Optional.ofNullable(req.getStars()).ifPresent(pref::setStars);
		Optional.ofNullable(req.getRashis()).ifPresent(pref::setRashis);

		Optional.ofNullable(req.getManglik()).ifPresent(pref::setManglik);
		Optional.ofNullable(req.getEducationType()).ifPresent(pref::setEducationType);
		Optional.ofNullable(req.getEducationLevels()).ifPresent(pref::setEducationLevels);
		Optional.ofNullable(req.getEmployedIn()).ifPresent(pref::setEmployedIn);
		Optional.ofNullable(req.getOccupations()).ifPresent(pref::setOccupations);
		Optional.ofNullable(req.getAnnualIncome()).ifPresent(pref::setAnnualIncome);
		Optional.ofNullable(req.getDietaryHabits()).ifPresent(pref::setDietaryHabits);
		Optional.ofNullable(req.getSmokingHabits()).ifPresent(pref::setSmokingHabits);
		Optional.ofNullable(req.getDrinkingHabits()).ifPresent(pref::setDrinkingHabits);
		Optional.ofNullable(req.getCitizenships()).ifPresent(pref::setCitizenships);
		Optional.ofNullable(req.getCountriesLivedIn()).ifPresent(pref::setCountriesLivedIn);
		Optional.ofNullable(req.getAboutMyPartner()).ifPresent(pref::setAboutMyPartner);
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdatePreference(ProfilePreferenceRequest request) {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			ProfilePreference pref = preferenceRepository.findByProfile(profile).orElse(new ProfilePreference());
			pref.setProfile(profile);

			updateFromRequest(pref, request);
			preferenceRepository.save(pref);
			profileService.calculateProfileCompletion(profile.getId());
			return new ResponseEntity("Preference saved successfully", 200, mapToResponse(pref));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error saving preference: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getPreferenceByLoggedInUser() {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			return preferenceRepository.findByProfile(profile)
					.map(pref -> new ResponseEntity("Preference found", 200, mapToResponse(pref)))
					.orElse(new ResponseEntity("Preference not set", 404, null));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error fetching preference: " + e.getMessage(), 500, null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deletePreference() {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			return preferenceRepository.findByProfile(profile).map(pref -> {
				preferenceRepository.delete(pref);
				return new ResponseEntity("Preference deleted successfully", 200, null);
			}).orElse(new ResponseEntity("Preference not set", 404, null));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error deleting preference: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getPreferenceById(Long id) {
		try {
			return preferenceRepository.findById(id)
					.map(pref -> new ResponseEntity("Preference found", 200, mapToResponse(pref)))
					.orElse(new ResponseEntity("Preference not found", 404, null));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error fetching preference: " + e.getMessage(), 500, null);
		}
	}
}
