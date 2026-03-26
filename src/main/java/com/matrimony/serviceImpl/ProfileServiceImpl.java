package com.matrimony.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.dto.request.ProfileRequest;
import com.matrimony.model.dto.response.EducationOccupationDetailsResponse;
import com.matrimony.model.dto.response.FamilyDetailsResponse;
import com.matrimony.model.dto.response.GalleryImageResponse;
import com.matrimony.model.dto.response.HobbiesAndInterestsResponse;
import com.matrimony.model.dto.response.LocationResponse;
import com.matrimony.model.dto.response.ProfilePreferenceResponse;
import com.matrimony.model.dto.response.ProfileResponse;
import com.matrimony.model.dto.response.UserResponse;
import com.matrimony.model.entity.EducationOccupationDetails;
import com.matrimony.model.entity.FamilyDetails;
import com.matrimony.model.entity.GalleryImage;
import com.matrimony.model.entity.HobbiesAndInterests;
import com.matrimony.model.entity.Location;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ProfilePreference;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.ProfileService;
import com.matrimony.service.UserService;
import com.matrimony.service.UserSubscriptionService;
import com.matrimony.util.ProfileCalculator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private UserService userService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ProfileCalculator profileCalculator;

	@Autowired
	private UserSubscriptionService userSubscriptionService;

	@Override
	public Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

	@Override
	public ResponseEntity getCurrentUserProfile() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = getCurrentUserId();
			Profile profile = profileRepository.findByUserId(userId).orElse(null);
			if (profile == null) {
				return new ResponseEntity("Profile not found for user: " + username, HttpStatus.NOT_FOUND.value(),
						null);
			}
			return new ResponseEntity("Profile fetched successfully", HttpStatus.OK.value(),
					convertToResponse(profile));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching current user profile: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

//	@Override
//	public ResponseEntity getProfileById(Long id) {
//		try {
//
//			boolean isPremiumUser = userSubscriptionService.hasAnActiveSubscription();
//
//			Profile profile = profileRepository.findById(id).orElse(null);
//			if (profile == null) {
//				return new ResponseEntity("Profile not found with id: " + id, HttpStatus.NOT_FOUND.value(), null);
//			}
//
//			if (!isPremiumUser) {
//
//				profile.setCaste(null);
//				profile.setSubCaste(null);
//				profile.setGothra(null);
//				profile.setStar(null);
//				profile.setRashi(null);
//				profile.setManglik(null);
//				if (profile.getFamilyDetails() != null) {
//					profile.getFamilyDetails().setParentsContactNo(null);
//				}
//				if (profile.getPreference() != null) {
//					profile.getPreference().setCastes(null);
//					profile.getPreference().setSubCastes(null);
//					profile.getPreference().setGothras(null);
//					profile.getPreference().setStars(null);
//					profile.getPreference().setRashis(null);
//					profile.getPreference().setManglik(null);
//				}
//			}
//			return new ResponseEntity("Profile fetched successfully", HttpStatus.OK.value(),
//					convertToResponse(profile));
//		} catch (Exception e) {
//			return new ResponseEntity("Error fetching profile by ID: " + e.getMessage(),
//					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
//		}
//	}
	
	public ResponseEntity getProfileById(Long id) {
	    try {

	        boolean isPremiumUser = userSubscriptionService.hasAnActiveSubscription();

	        Profile profile = profileRepository.findById(id).orElse(null);
	        if (profile == null) {
	            return new ResponseEntity("Profile not found with id: " + id, HttpStatus.NOT_FOUND.value(), null);
	        }
	        ProfileResponse response = convertToResponse(profile);
	        if (!isPremiumUser) {

	            response.setCaste(null);
	            response.setSubCaste(null);
	            response.setGothra(null);
	            response.setStar(null);
	            response.setRashi(null);
	            response.setManglik(null);

	            if (response.getFamilyDetails() != null) {
	                response.getFamilyDetails().setParentsContactNo(null);
	            }

	            if (response.getPreference() != null) {
	                response.getPreference().setCastes(null);
	                response.getPreference().setSubCastes(null);
	                response.getPreference().setGothras(null);
	                response.getPreference().setStars(null);
	                response.getPreference().setRashis(null);
	                response.getPreference().setManglik(null);
	            }
	        }

	        return new ResponseEntity("Profile fetched successfully", HttpStatus.OK.value(), response);

	    } catch (Exception e) {
	        return new ResponseEntity(
	                "Error fetching profile by ID: " + e.getMessage(),
	                HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                null
	        );
	    }
	}


	@Override
	public ResponseEntity getProfileByUserId(Long userId) {
		try {
			ResponseEntity userResponse = userService.getUserById(userId);
			if (userResponse.getPayload() == null) {
				return new ResponseEntity("User not found with id: " + userId, HttpStatus.NOT_FOUND.value(), null);
			}
			UserResponse userDto = (UserResponse) userResponse.getPayload();
			Profile profile = profileRepository.findByUserId(userDto.getId()).orElse(null);
			if (profile == null) {
				return new ResponseEntity("Profile not found for user id: " + userId, HttpStatus.NOT_FOUND.value(),
						null);
			}
			return new ResponseEntity("Profile fetched successfully", HttpStatus.OK.value(),
					convertToResponse(profile));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching profile by user ID: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity createProfile(ProfileRequest profileRequest) {
		System.out.println(profileRequest.toString());
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
//			User user = userService.getUserByUsername(username);
			Long userId = getCurrentUserId();
			if (profileRepository.findByUserId(userId).isPresent()) {
				return new ResponseEntity("Profile already exists for user: " + username, 400, null);
			}

			User userRef = entityManager.getReference(User.class, userId);

			Profile profile = convertToEntity(profileRequest);
			profile.setUser(userRef);
			profile.setCreatedAt(LocalDateTime.now());
			profile.setUpdatedAt(LocalDateTime.now());

			Profile savedProfile = profileRepository.save(profile);
			calculateProfileCompletion(savedProfile.getId());

			return new ResponseEntity("Profile created successfully", 201, convertToResponse(savedProfile));
		} catch (Exception e) {

			System.err.println(e);

			return new ResponseEntity("Error creating profile: " + e.getMessage(), 500, null);
		}
	}

//	@Override
//	public ResponseEntity updateProfile(Long id, ProfileRequest profileRequest) {
//		try {
//			Profile profile = profileRepository.findById(id).orElse(null);
//
//			if (profile == null) {
//				return new ResponseEntity("Profile not found with id: " + id, HttpStatus.NOT_FOUND.value(), null);
//			}
//
//			String username = SecurityContextHolder.getContext().getAuthentication().getName();
//			if (!profile.getUser().getUsername().equals(username)) {
//				return new ResponseEntity("You can only update your own profile", HttpStatus.FORBIDDEN.value(), null);
//			}
//
//			updateEntityFromRequest(profile, profileRequest);
//			profile.setUpdatedAt(LocalDateTime.now());
//
//			Profile updatedProfile = profileRepository.save(profile);
//			calculateProfileCompletion(updatedProfile.getId());
//
//			return new ResponseEntity("Profile updated successfully", HttpStatus.OK.value(),
//					convertToResponse(updatedProfile));
//
//		} catch (Exception e) {
//			return new ResponseEntity("Error updating profile: " + e.getMessage(),
//					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
//		}
//	}

	@Override
	public ResponseEntity updateProfile(ProfileRequest profileRequest) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = getCurrentUserId();

			Profile profile = profileRepository.findByUserId(userId).orElse(null);

			if (profile == null) {
				User userRef = entityManager.getReference(User.class, userId);

				profile = convertToEntity(profileRequest);
				profile.setUser(userRef);
				profile.setCreatedAt(LocalDateTime.now());
				profile.setUpdatedAt(LocalDateTime.now());

				profile = profileRepository.save(profile);
				calculateProfileCompletion(profile.getId());

				return new ResponseEntity("Profile created successfully", 201, convertToResponse(profile));
			} else {
				if (!profile.getUser().getUsername().equals(username)) {
					return new ResponseEntity("You can only update your own profile", HttpStatus.FORBIDDEN.value(),
							null);
				}

				updateEntityFromRequest(profile, profileRequest);
				profile.setUpdatedAt(LocalDateTime.now());

				Profile updatedProfile = profileRepository.save(profile);
				calculateProfileCompletion(updatedProfile.getId());

				return new ResponseEntity("Profile updated successfully", HttpStatus.OK.value(),
						convertToResponse(updatedProfile));
			}

		} catch (Exception e) {
			return new ResponseEntity("Error processing profile: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity deleteProfile(Long id) {
		try {
			Profile profile = profileRepository.findById(id).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found with id: " + id, HttpStatus.NOT_FOUND.value(), null);
			}

			profileRepository.delete(profile);

			return new ResponseEntity("Profile deleted successfully", HttpStatus.OK.value(), null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting profile: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity calculateProfileCompletion(Long profileId) {
		try {
			Profile profile = profileRepository.findById(profileId).orElse(null);

			if (profile == null) {
				return new ResponseEntity("Profile not found with id: " + profileId, HttpStatus.NOT_FOUND.value(),
						null);
			}

			int completionPercentage = profileCalculator.calculateProfileCompletionPercentage(profile);
			profile.setProfileCompletionPercentage(completionPercentage);
			profile.setProfileComplete(completionPercentage >= 80);

			profileRepository.save(profile);

			return new ResponseEntity("Profile completion calculated", HttpStatus.OK.value(), completionPercentage);

		} catch (Exception e) {
			return new ResponseEntity("Error calculating profile completion: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getLocationAndOccupation(Long profileId) {
		try {
			Optional<Profile> optionalProfile = profileRepository.findById(profileId);

			if (!optionalProfile.isPresent()) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			Profile profile = optionalProfile.get();

			// Fetch related entities safely
			String occupation = null;
			String highestEducation = null;
			if (profile.getEducationOccupationDetails() != null) {
				occupation = profile.getEducationOccupationDetails().getOccupation();
				highestEducation = profile.getEducationOccupationDetails().getHighestEducation();
			}

			String city = null;
			String country = null;
			if (profile.getLocation() != null) {
				city = profile.getLocation().getCity();
				country = profile.getLocation().getCountry();
			}

			Map<String, Object> payload = new HashMap<>();
			payload.put("occupation", occupation);
			payload.put("city", city);
			payload.put("country", country);
		    payload.put("highestEducation", highestEducation);

			return new ResponseEntity("Profile summary fetched", 200, payload);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error fetching profile summary: " + e.getMessage(), 500, null);
		}
	}
	// Utility Methods

	private Profile convertToEntity(ProfileRequest request) {
		Profile profile = new Profile();
		updateEntityFromRequest(profile, request);
		return profile;
	}

	private void updateEntityFromRequest(Profile profile, ProfileRequest request) {
		profile.setFirstName(request.getFirstName());
		profile.setLastName(request.getLastName());
		profile.setGender(request.getGender());
		profile.setProfileCreatedFor(request.getProfileCreatedFor());
		profile.setDateOfBirth(request.getDateOfBirth());
		profile.setTimeOfBirth(request.getTimeOfBirth());
		profile.setPlaceOfBirth(request.getPlaceOfBirth());
		profile.setReligion(request.getReligion());
		profile.setCaste(request.getCaste());
		profile.setSubCaste(request.getSubCaste());
		profile.setMaritalStatus(request.getMaritalStatus());
		profile.setHeightIn(request.getHeightIn());
		profile.setWeight(request.getWeight());
		profile.setPhysicalStatus(request.getPhysicalStatus());
		profile.setMotherTongue(request.getMotherTongue());
		profile.setLanguagesKnown(request.getLanguagesKnown());
		profile.setGothra(request.getGothra());
		profile.setStar(request.getStar());
		profile.setRashi(request.getRashi());
		profile.setManglik(request.getManglik());
		profile.setAbout(request.getAbout());
		profile.setDietaryHabits(request.getDietaryHabits());
		profile.setDrinkingHabits(request.getDrinkingHabits());
		profile.setSmokingHabits(request.getSmokingHabits());
		profile.setHasDisease(request.getHasDisease());
		
		if(Boolean.TRUE.equals(request.getHasDisease())) {
			profile.setDiseaseDetails(request.getDiseaseDetails());
		}else {
			profile.setDiseaseDetails(null);
		}
		profile.setDeclarationAccepted(request.getDeclarationAccepted());
		if(Boolean.TRUE.equals(request.getDeclarationAccepted())) {
			profile.setDeclarationText(request.getDeclarationText());
		}else {
			profile.setDeclarationText(null);
		}
	}

	private ProfileResponse convertToResponse(Profile profile) {
		ProfileResponse response = new ProfileResponse();
		response.setId(profile.getId());
		response.setUserId(profile.getUser().getId());
		response.setUsername(profile.getUser().getUsername());
		response.setEmail(profile.getUser().getEmail());
		response.setFirstName(profile.getFirstName());
		response.setLastName(profile.getLastName());
		response.setGender(profile.getGender());
		response.setProfileCreatedFor(profile.getProfileCreatedFor());
		response.setDateOfBirth(profile.getDateOfBirth());
		response.setTimeOfBirth(profile.getTimeOfBirth());
		response.setPlaceOfBirth(profile.getPlaceOfBirth());
		response.setAge(profileCalculator.calculateAge(profile.getDateOfBirth()));
		response.setReligion(profile.getReligion());
		response.setCaste(profile.getCaste());
		response.setSubCaste(profile.getSubCaste());
		response.setMaritalStatus(profile.getMaritalStatus());
		response.setHeightIn(profile.getHeightIn());
		response.setWeight(profile.getWeight());
		response.setPhysicalStatus(profile.getPhysicalStatus());
		response.setMotherTongue(profile.getMotherTongue());
		response.setLanguagesKnown(profile.getLanguagesKnown());
		response.setGothra(profile.getGothra());
		response.setStar(profile.getStar());
		response.setRashi(profile.getRashi());
		response.setManglik(profile.getManglik());
		response.setAbout(profile.getAbout());
		response.setProfilePictureUrl(profile.getProfilePictureUrl());
		response.setDietaryHabits(profile.getDietaryHabits());
		response.setDrinkingHabits(profile.getDrinkingHabits());
		response.setSmokingHabits(profile.getSmokingHabits());
		response.setVerificationStatus(profile.getVerificationStatus());
		response.setProfileComplete(profile.getProfileComplete());
		response.setProfileCompletionPercentage(profile.getProfileCompletionPercentage());
		response.setCreatedAt(profile.getCreatedAt());
		response.setUpdatedAt(profile.getUpdatedAt());
		response.setHasDisease(profile.getHasDisease());
		response.setDiseaseDetails(profile.getDiseaseDetails());
		response.setDeclarationAccepted(profile.getDeclarationAccepted());
		response.setDeclarationText(profile.getDeclarationText());

		// Map preference only if exists
		if (profile.getPreference() != null) {
			response.setPreference(mapPreference(profile.getPreference()));
		}

		// Map images only if exist
		if (profile.getImages() != null && !profile.getImages().isEmpty()) {
			response.setImages(mapImages(profile.getImages()));
		}

		// Map family details only if exists
		if (profile.getFamilyDetails() != null) {
			response.setFamilyDetails(mapFamilyDetails(profile.getFamilyDetails()));
		}

		if (profile.getHobbiesAndInterests() != null) {
			response.setHobbiesAndInterestsResponse(mapHobbiesAndInterest(profile.getHobbiesAndInterests()));
		}

		if (profile.getEducationOccupationDetails() != null) {
			response.setEducationOccupationDetailsResponse(
					mapEducationOccuption(profile.getEducationOccupationDetails()));
		}
		if (profile.getLocation() != null) {
			response.setLocationResponse(mapLocation(profile.getLocation()));
		}

		return response;
	}

	private ProfilePreferenceResponse mapPreference(ProfilePreference pref) {
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

//	private String formatHeight(Integer inches) {
//		if (inches == null)
//			return null;
//		int feet = inches / 12;
//		int remainingInches = inches % 12;
//		return feet + " feet " + remainingInches + " inches";
//	}

	private List<GalleryImageResponse> mapImages(List<GalleryImage> images) {
		return images.stream().map(img -> {
			GalleryImageResponse imgResp = new GalleryImageResponse();
			imgResp.setId(img.getId());
			imgResp.setImageUrl(img.getImageUrl());
			imgResp.setUploadedAt(img.getUploadedAt());
			imgResp.setProfileId(img.getProfile().getId().toString());
			imgResp.setImageFormat(img.getImageFormat());
			imgResp.setImageSize(img.getImageSize().toString());

			return imgResp;
		}).toList();
	}

	private FamilyDetailsResponse mapFamilyDetails(FamilyDetails fam) {
		FamilyDetailsResponse famResp = new FamilyDetailsResponse();
		famResp.setId(fam.getId());
		famResp.setFamilyValue(fam.getFamilyValue());
		famResp.setFamilyType(fam.getFamilyType());
		famResp.setFamilyStatus(fam.getFamilyStatus());
		famResp.setFatherOccupation(fam.getFatherOccupation());
		famResp.setMotherOccupation(fam.getMotherOccupation());
		famResp.setNativePlace(fam.getNativePlace());
		famResp.setNoOfBrothers(fam.getNoOfBrothers());
		famResp.setBrothersMarried(fam.getBrothersMarried());
		famResp.setNoOfSisters(fam.getNoOfSisters());
		famResp.setSistersMarried(fam.getSistersMarried());
		famResp.setParentsContactNo(fam.getParentsContactNo());
		famResp.setGrandFatherOccupation(fam.getGrandFatherOccupation());
		famResp.setGrandMotherOccupation(fam.getGrandMotherOccupation());
		return famResp;
	}

	private HobbiesAndInterestsResponse mapHobbiesAndInterest(HobbiesAndInterests entity) {
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

	private EducationOccupationDetailsResponse mapEducationOccuption(EducationOccupationDetails details) {
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
		return response;
	}

	private LocationResponse mapLocation(Location location) {
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
