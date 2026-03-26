package com.matrimony.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.LocationRequest;
import com.matrimony.model.dto.response.LocationResponse;
import com.matrimony.model.entity.Location;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.LocationRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.LocationService;
import com.matrimony.service.ProfileService;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepository;

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
	public ResponseEntity getLocationForCurrentUser() {
		try {
			Long userId = getCurrentUserId();
			Optional<Profile> profileOpt = profileRepository.findByUserId(userId);

			if (profileOpt.isEmpty() || profileOpt.get().getLocation() == null) {
				return new ResponseEntity("Location not found", 404, null);
			}

			LocationResponse response = convertToResponse(profileOpt.get().getLocation());
			return new ResponseEntity("Location fetched successfully", 200, response);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching location: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity createLocationForCurrentUser(LocationRequest request) {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			if (profile.getLocation() != null) {
				return new ResponseEntity("Location already exists", 400, null);
			}

			Location location = new Location();
			mapRequestToLocation(request, location);
			location.setProfile(profile);
			locationRepository.save(location);

			profile.setLocation(location);
			profileRepository.save(profile);
			
			profileService.calculateProfileCompletion(profile.getId());
			return new ResponseEntity("Location created successfully", 201, convertToResponse(location));

		} catch (Exception e) {
			return new ResponseEntity("Error creating location: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateLocationForCurrentUser(LocationRequest request) {
	    try {
	        Long userId = getCurrentUserId();
	        Profile profile = profileRepository.findByUserId(userId).orElse(null);

	        if (profile == null) {
	            return new ResponseEntity("Profile not found", 404, null);
	        }

	        Location location = profile.getLocation();

	        if (location == null) {
	            location = new Location();
	            mapRequestToLocation(request, location);
	            location.setProfile(profile);
	            locationRepository.save(location);

	            profile.setLocation(location);
	            profileRepository.save(profile);
	            profileService.calculateProfileCompletion(profile.getId());
	            return new ResponseEntity("Location created successfully", 201, convertToResponse(location));
	        } else {
	            mapRequestToLocation(request, location);
	            locationRepository.save(location);
	            profileService.calculateProfileCompletion(profile.getId());

	            return new ResponseEntity("Location updated successfully", 200, convertToResponse(location));
	        }

	    } catch (Exception e) {
	        return new ResponseEntity("Error processing location: " + e.getMessage(), 500, null);
	    }
	}

	@Override
	public ResponseEntity deleteLocationForCurrentUser() {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null || profile.getLocation() == null) {
				return new ResponseEntity("Location not found", 404, null);
			}

			Location location = profile.getLocation();
			profile.setLocation(null);
			profileRepository.save(profile);

			locationRepository.delete(location);

			return new ResponseEntity("Location deleted successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting location: " + e.getMessage(), 500, null);
		}
	}

	private void mapRequestToLocation(LocationRequest request, Location location) {
		location.setCity(request.getCity());
		location.setState(request.getState());
		location.setCountry(request.getCountry());
		location.setPostalCode(request.getPostalCode());
		location.setCitizenship(request.getCitizenship());
		location.setResidencyStatus(request.getResidencyStatus());
		location.setLivingSinceYear(request.getLivingSinceYear());
	}

	private LocationResponse convertToResponse(Location location) {
		LocationResponse response = new LocationResponse();
		response.setId(location.getId());
		response.setCity(location.getCity());
		response.setState(location.getState());
		response.setCountry(location.getCountry());
		response.setPostalCode(location.getPostalCode());
		response.setCitizenship(location.getCitizenship());
		response.setResidencyStatus(location.getResidencyStatus());
		response.setLivingSinceYear(location.getLivingSinceYear());
		return response;
	}

}
