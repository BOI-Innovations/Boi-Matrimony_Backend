package com.matrimony.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.EducationOccupationDetailsRequest;
import com.matrimony.model.dto.response.EducationOccupationDetailsResponse;
import com.matrimony.model.entity.EducationOccupationDetails;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.EducationOccupationDetailsRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.EducationOccupationDetailsService;
import com.matrimony.service.ProfileService;

@Service
public class EducationOccupationDetailsServiceImpl implements EducationOccupationDetailsService {

	@Autowired
	private EducationOccupationDetailsRepository repository;

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
	public ResponseEntity getEducationOccupationDetailsForCurrentUser() {
		try {
			Long userId = getCurrentUserId();
			Optional<Profile> profileOpt = profileRepository.findByUserId(userId);

			if (profileOpt.isEmpty() || profileOpt.get().getEducationOccupationDetails() == null) {
				return new ResponseEntity("Education & Occupation details not found", 404, null);
			}

			EducationOccupationDetailsResponse response = convertToResponse(
					profileOpt.get().getEducationOccupationDetails());
			return new ResponseEntity("Details fetched successfully", 200, response);

		} catch (Exception e) {
			return new ResponseEntity("Error fetching details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity createEducationOccupationDetailsForCurrentUser(EducationOccupationDetailsRequest request) {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null)
				return new ResponseEntity("Profile not found", 404, null);

			if (profile.getEducationOccupationDetails() != null) {
				return new ResponseEntity("Details already exist", 400, null);
			}

			EducationOccupationDetails details = new EducationOccupationDetails();
			mapRequestToEntity(request, details);
			details.setProfile(profile);
			repository.save(details);

			profile.setEducationOccupationDetails(details);
			profileRepository.save(profile);
			profileService.calculateProfileCompletion(profile.getId());

			return new ResponseEntity("Details created successfully", 201, convertToResponse(details));

		} catch (Exception e) {
			return new ResponseEntity("Error creating details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateEducationOccupationDetailsForCurrentUser(EducationOccupationDetailsRequest request) {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			EducationOccupationDetails details = profile.getEducationOccupationDetails();

			if (details == null) {
				details = new EducationOccupationDetails();
				mapRequestToEntity(request, details);
				details.setProfile(profile);
				repository.save(details);

				profile.setEducationOccupationDetails(details);
				profileRepository.save(profile);
				profileService.calculateProfileCompletion(profile.getId());

				return new ResponseEntity("Details created successfully", 201, convertToResponse(details));
			} else {
				mapRequestToEntity(request, details);
				repository.save(details);
				profileService.calculateProfileCompletion(profile.getId());
				return new ResponseEntity("Details updated successfully", 200, convertToResponse(details));
			}

		} catch (Exception e) {
			return new ResponseEntity("Error processing details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deleteEducationOccupationDetailsForCurrentUser() {
		try {
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null || profile.getEducationOccupationDetails() == null) {
				return new ResponseEntity("Details not found", 404, null);
			}

			EducationOccupationDetails details = profile.getEducationOccupationDetails();
			profile.setEducationOccupationDetails(null);
			profileRepository.save(profile);

			repository.delete(details);

			return new ResponseEntity("Details deleted successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting details: " + e.getMessage(), 500, null);
		}
	}

	private void mapRequestToEntity(EducationOccupationDetailsRequest request, EducationOccupationDetails details) {
		details.setHighestEducation(request.getHighestEducation());
		details.setAdditionalDegree(request.getAdditionalDegree());
		details.setCollegeInstitution(request.getCollegeInstitution());
		details.setEducationInDetail(request.getEducationInDetail());
		details.setEmployedIn(request.getEmployedIn());
		details.setOccupation(request.getOccupation());
		details.setOccupationInDetail(request.getOccupationInDetail());
		details.setAnnualIncome(request.getAnnualIncome());
		details.setIncomeCurrency(request.getIncomeCurrency());
		details.setWorkCity(request.getWorkCity());
		details.setWorkCountry(request.getWorkCountry());
		details.setOrganizationName(request.getOrganizationName());
	}

	private EducationOccupationDetailsResponse convertToResponse(EducationOccupationDetails details) {
		EducationOccupationDetailsResponse response = new EducationOccupationDetailsResponse();
		response.setId(details.getId());
		response.setHighestEducation(details.getHighestEducation());
		response.setAdditionalDegree(details.getAdditionalDegree());
		response.setCollegeInstitution(details.getCollegeInstitution());
		response.setEducationInDetail(details.getEducationInDetail());
		response.setEmployedIn(details.getEmployedIn());
		response.setOccupation(details.getOccupation());
		response.setOccupationInDetail(details.getOccupationInDetail());
		response.setAnnualIncome(details.getAnnualIncome());
		response.setIncomeCurrency(details.getIncomeCurrency());
		response.setWorkCity(details.getWorkCity());
		response.setWorkCountry(details.getWorkCountry());
		response.setOrganizationName(details.getOrganizationName());
		return response;
	}

}
