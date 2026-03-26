package com.matrimony.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.HobbiesAndInterestsRequest;
import com.matrimony.model.dto.response.HobbiesAndInterestsResponse;
import com.matrimony.model.entity.HobbiesAndInterests;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.HobbiesAndInterestsRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.HobbiesAndInterestsService;
import com.matrimony.service.ProfileService;

@Service
public class HobbiesAndInterestsServiceImpl implements HobbiesAndInterestsService {

	@Autowired
	private HobbiesAndInterestsRepository hobbiesAndInterestsRepository;

	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private ProfileService profileService;

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

	@Override
	public ResponseEntity getHobbiesAndInterestsForCurrentUser() {
		try {
			Long userId = getCurrentUserId();
			Optional<Profile> profileOpt = profileRepository.findByUserId(userId);

			if (profileOpt.isEmpty() || profileOpt.get().getHobbiesAndInterests() == null) {
				return new ResponseEntity("Hobbies and interests not found", 404, null);
			}

			HobbiesAndInterestsResponse response = convertToResponse(profileOpt.get().getHobbiesAndInterests());
			return new ResponseEntity("Fetched successfully", 200, response);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching hobbies & interests: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity createHobbiesAndInterestsForCurrentUser(HobbiesAndInterestsRequest request) {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			if (profile.getHobbiesAndInterests() != null) {
				return new ResponseEntity("Hobbies & interests already exist", 400, null);
			}

			HobbiesAndInterests hobbiesAndInterests = new HobbiesAndInterests();
			mapRequestToEntity(request, hobbiesAndInterests);
			hobbiesAndInterests.setProfile(profile);
			hobbiesAndInterestsRepository.save(hobbiesAndInterests);

			profile.setHobbiesAndInterests(hobbiesAndInterests);
			profileRepository.save(profile);
			profileService.calculateProfileCompletion(profile.getId());
			return new ResponseEntity("Created successfully", 201, convertToResponse(hobbiesAndInterests));

		} catch (Exception e) {
			return new ResponseEntity("Error creating hobbies & interests: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateHobbiesAndInterestsForCurrentUser(HobbiesAndInterestsRequest request) {
	    try {
	        Long userId = getCurrentUserId();
	        Profile profile = profileRepository.findByUserId(userId).orElse(null);

	        if (profile == null) {
	            return new ResponseEntity("Profile not found", 404, null);
	        }

	        HobbiesAndInterests hobbiesAndInterests = profile.getHobbiesAndInterests();

	        if (hobbiesAndInterests == null) {
	            hobbiesAndInterests = new HobbiesAndInterests();
	            mapRequestToEntity(request, hobbiesAndInterests);
	            hobbiesAndInterests.setProfile(profile);
	            hobbiesAndInterestsRepository.save(hobbiesAndInterests);

	            profile.setHobbiesAndInterests(hobbiesAndInterests);
	            profileRepository.save(profile);
	            profileService.calculateProfileCompletion(profile.getId());
	            return new ResponseEntity("Created successfully", 201, convertToResponse(hobbiesAndInterests));
	        } else {
	            mapRequestToEntity(request, hobbiesAndInterests);
	            hobbiesAndInterestsRepository.save(hobbiesAndInterests);
	            profileService.calculateProfileCompletion(profile.getId());
	            return new ResponseEntity("Updated successfully", 200, convertToResponse(hobbiesAndInterests));
	        }

	    } catch (Exception e) {
	        return new ResponseEntity("Error processing hobbies & interests: " + e.getMessage(), 500, null);
	    }
	}


	@Override
	public ResponseEntity deleteHobbiesAndInterestsForCurrentUser() {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null || profile.getHobbiesAndInterests() == null) {
				return new ResponseEntity("Hobbies & interests not found", 404, null);
			}

			HobbiesAndInterests hobbiesAndInterests = profile.getHobbiesAndInterests();
			profile.setHobbiesAndInterests(null);
			profileRepository.save(profile);

			hobbiesAndInterestsRepository.delete(hobbiesAndInterests);

			return new ResponseEntity("Deleted successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting hobbies & interests: " + e.getMessage(), 500, null);
		}
	}

	private void mapRequestToEntity(HobbiesAndInterestsRequest request, HobbiesAndInterests entity) {
		entity.setHobbies(request.getHobbies());
		entity.setOtherHobbies(request.getOtherHobbies());
		entity.setFavouriteMusic(request.getFavouriteMusic());
		entity.setOtherMusic(request.getOtherMusic());
		entity.setSports(request.getSports());
		entity.setOtherSports(request.getOtherSports());
		entity.setFavouriteFood(request.getFavouriteFood());
		entity.setOtherFood(request.getOtherFood());
	}

	private HobbiesAndInterestsResponse convertToResponse(HobbiesAndInterests entity) {
		HobbiesAndInterestsResponse response = new HobbiesAndInterestsResponse();
		response.setId(entity.getId());
		response.setHobbies(entity.getHobbies());
		response.setOtherHobbies(entity.getOtherHobbies());
		response.setFavouriteMusic(entity.getFavouriteMusic());
		response.setOtherMusic(entity.getOtherMusic());
		response.setSports(entity.getSports());
		response.setOtherSports(entity.getOtherSports());
		response.setFavouriteFood(entity.getFavouriteFood());
		response.setOtherFood(entity.getOtherFood());
		return response;
	}
}
