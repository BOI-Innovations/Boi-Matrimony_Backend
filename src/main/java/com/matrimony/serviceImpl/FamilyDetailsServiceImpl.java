package com.matrimony.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.FamilyDetailsRequest;
import com.matrimony.model.dto.response.FamilyDetailsResponse;
import com.matrimony.model.entity.FamilyDetails;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.FamilyDetailsRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.FamilyDetailsService;
import com.matrimony.service.ProfileService;

@Service
public class FamilyDetailsServiceImpl implements FamilyDetailsService {

	@Autowired
	private FamilyDetailsRepository familyDetailsRepository;

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
	public ResponseEntity getFamilyDetailsForCurrentUser() {
		try {
			Long userId = getCurrentUserId();
			Optional<Profile> profileOpt = profileRepository.findByUserId(userId);

			if (profileOpt.isEmpty() || profileOpt.get().getFamilyDetails() == null) {
				return new ResponseEntity("Family details not found", 404, null);
			}

			FamilyDetailsResponse response = convertToResponse(profileOpt.get().getFamilyDetails());
			return new ResponseEntity("Family details fetched successfully", 200, response);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching family details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity createFamilyDetailsForCurrentUser(FamilyDetailsRequest request) {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			if (profile.getFamilyDetails() != null) {
				return new ResponseEntity("Family details already exist", 400, null);
			}

			FamilyDetails familyDetails = new FamilyDetails();
			mapRequestToFamilyDetails(request, familyDetails);
			familyDetails.setProfile(profile);
			familyDetailsRepository.save(familyDetails);

			profile.setFamilyDetails(familyDetails);
			profileRepository.save(profile);
			profileService.calculateProfileCompletion(profile.getId());
			return new ResponseEntity("Family details created successfully", 201, convertToResponse(familyDetails));

		} catch (Exception e) {
			return new ResponseEntity("Error creating family details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateFamilyDetailsForCurrentUser(FamilyDetailsRequest request) {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			FamilyDetails familyDetails = profile.getFamilyDetails();

			if (familyDetails == null) {
				familyDetails = new FamilyDetails();
				mapRequestToFamilyDetails(request, familyDetails);
				familyDetails.setProfile(profile);
				familyDetailsRepository.save(familyDetails);

				profile.setFamilyDetails(familyDetails);
				profileRepository.save(profile);
				profileService.calculateProfileCompletion(profile.getId());
				
				return new ResponseEntity("Family details created successfully", 201, convertToResponse(familyDetails));
			} else {
				mapRequestToFamilyDetails(request, familyDetails);
				familyDetailsRepository.save(familyDetails);
				profileService.calculateProfileCompletion(profile.getId());
				return new ResponseEntity("Family details updated successfully", 200, convertToResponse(familyDetails));
			}

		} catch (Exception e) {
			return new ResponseEntity("Error processing family details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateParentsContactNumber(String contactNumber) {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null || profile.getFamilyDetails() == null) {
				return new ResponseEntity("Family details not found", 404, null);
			}

			if (contactNumber == null || contactNumber.trim().isEmpty()) {
				return new ResponseEntity("Contact number cannot be empty", 400, null);
			}

			FamilyDetails familyDetails = profile.getFamilyDetails();
			familyDetails.setParentsContactNo(contactNumber);

			familyDetailsRepository.save(familyDetails);
			profileService.calculateProfileCompletion(profile.getId());
			return new ResponseEntity("Family details updated successfully", 200, convertToResponse(familyDetails));

		} catch (Exception e) {
			return new ResponseEntity("Error updating family details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deleteFamilyDetailsForCurrentUser() {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null || profile.getFamilyDetails() == null) {
				return new ResponseEntity("Family details not found", 404, null);
			}

			FamilyDetails familyDetails = profile.getFamilyDetails();
			profile.setFamilyDetails(null);
			profileRepository.save(profile);

			familyDetailsRepository.delete(familyDetails);

			return new ResponseEntity("Family details deleted successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting family details: " + e.getMessage(), 500, null);
		}
	}

	private void mapRequestToFamilyDetails(FamilyDetailsRequest request, FamilyDetails familyDetails) {
		familyDetails.setParentsContactNo(request.getParentsContactNo());
		familyDetails.setFatherOccupation(request.getFatherOccupation());
		familyDetails.setMotherOccupation(request.getMotherOccupation());
		familyDetails.setFamilyType(request.getFamilyType());
		familyDetails.setFamilyStatus(request.getFamilyStatus());
		familyDetails.setFamilyValue(request.getFamilyValue());
		familyDetails.setNativePlace(request.getNativePlace());
		familyDetails.setNoOfBrothers(request.getNoOfBrothers());
		familyDetails.setBrothersMarried(request.getBrothersMarried());
		familyDetails.setNoOfSisters(request.getNoOfSisters());
		familyDetails.setSistersMarried(request.getSistersMarried());
		familyDetails.setAboutMyFamily(request.getAboutMyFamily());
		familyDetails.setParentsContactNo(request.getParentsContactNo());
		familyDetails.setAboutMyFamily(request.getAboutMyFamily());
		familyDetails.setGrandFatherOccupation(request.getGrandFatherOccupation());
		familyDetails.setGrandMotherOccupation(request.getGrandMotherOccupation());
	}

	private FamilyDetailsResponse convertToResponse(FamilyDetails familyDetails) {
		FamilyDetailsResponse response = new FamilyDetailsResponse();
		response.setId(familyDetails.getId());
		response.setParentsContactNo(familyDetails.getParentsContactNo());
		response.setFatherOccupation(familyDetails.getFatherOccupation());
		response.setMotherOccupation(familyDetails.getMotherOccupation());
		response.setFamilyType(familyDetails.getFamilyType());
		response.setFamilyStatus(familyDetails.getFamilyStatus());
		response.setFamilyValue(familyDetails.getFamilyValue());
		response.setNativePlace(familyDetails.getNativePlace());
		response.setNoOfBrothers(familyDetails.getNoOfBrothers());
		response.setBrothersMarried(familyDetails.getBrothersMarried());
		response.setNoOfSisters(familyDetails.getNoOfSisters());
		response.setSistersMarried(familyDetails.getSistersMarried());
		response.setAboutMyFamily(familyDetails.getAboutMyFamily());
		response.setParentsContactNo(familyDetails.getParentsContactNo());
		response.setAboutMyFamily(familyDetails.getAboutMyFamily());
		response.setGrandFatherOccupation(familyDetails.getGrandFatherOccupation());
		response.setGrandMotherOccupation(familyDetails.getGrandMotherOccupation());
		return response;
	}
}
