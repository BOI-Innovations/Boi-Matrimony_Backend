package com.matrimony.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.SearchRequest;
import com.matrimony.model.dto.response.ProfileResponse;
import com.matrimony.model.dto.response.SearchResponse;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.User;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.service.SearchService;
import com.matrimony.service.UserService;
import com.matrimony.util.ProfileCalculator;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private UserService userService;

//	@Autowired
//	private MatchAlgorithm matchAlgorithm;

	@Autowired
	private ProfileCalculator profileCalculator;

	@Override
	public SearchResponse searchProfiles(SearchRequest searchRequest, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Profile> profilePage = profileRepository.searchProfiles(searchRequest.getGender(),
				searchRequest.getMinAge(), searchRequest.getMaxAge(), searchRequest.getReligion(),
				searchRequest.getCaste(), searchRequest.getMaritalStatus(), searchRequest.getEducation(),
				searchRequest.getOccupation(), searchRequest.getCity(), searchRequest.getCountry(), pageable);

		List<ProfileResponse> profiles = profilePage.getContent().stream().map(this::convertToProfileResponse)
				.collect(Collectors.toList());

		SearchResponse response = new SearchResponse();
		response.setProfiles(profiles);
		response.setTotalCount(profilePage.getTotalElements());
		response.setCurrentPage(page);
		response.setTotalPages(profilePage.getTotalPages());
		response.setPageSize(size);
		response.setHasNext(profilePage.hasNext());
		response.setHasPrevious(profilePage.hasPrevious());

		return response;
	}

	@Override
	public List<ProfileResponse> getMatches() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User currentUser = userService.getUserByUsername(username);
		Profile currentProfile = profileRepository.findByUserId(currentUser.getId())
				.orElseThrow(() -> new RuntimeException("Profile not found for user: " + username));

		List<Profile> potentialMatches = profileRepository.findPotentialMatches(currentProfile.getGender(),
				currentProfile.getReligion(), currentProfile.getCaste(), currentProfile.getMaritalStatus(),
				PageRequest.of(0, 20));

		return potentialMatches.stream().map(this::convertToProfileResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProfileResponse> getSuggestions() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User currentUser = userService.getUserByUsername(username);
		Profile currentProfile = profileRepository.findByUserId(currentUser.getId())
				.orElseThrow(() -> new RuntimeException("Profile not found for user: " + username));

		String city = currentProfile.getLocation() != null ? currentProfile.getLocation().getCity() : null;
		String education = currentProfile.getEducationOccupationDetails() != null
				? currentProfile.getEducationOccupationDetails().getHighestEducation()
				: null;

		List<Profile> suggestions = profileRepository.findSuggestions(currentProfile.getId(),
				currentProfile.getGender(), city, education, PageRequest.of(0, 10));

		return suggestions.stream().map(this::convertToProfileResponse).collect(Collectors.toList());
	}

	@Override
	public SearchResponse getNearbyProfiles(String city, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<Profile> profilePage = profileRepository.findByCityIgnoreCase(city, pageable);

		List<ProfileResponse> profiles = profilePage.getContent().stream().map(this::convertToProfileResponse)
				.collect(Collectors.toList());

		SearchResponse response = new SearchResponse();
		response.setProfiles(profiles);
		response.setTotalCount(profilePage.getTotalElements());
		response.setCurrentPage(page);
		response.setTotalPages(profilePage.getTotalPages());
		response.setPageSize(size);
		response.setHasNext(profilePage.hasNext());
		response.setHasPrevious(profilePage.hasPrevious());

		return response;
	}


	private ProfileResponse convertToProfileResponse(Profile profile) {
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

		return response;
	}
}