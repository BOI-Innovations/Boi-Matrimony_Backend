package com.matrimony.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrimony.model.dto.request.MatchResult;
import com.matrimony.model.dto.request.ProfileFilterRequest;
import com.matrimony.model.entity.EducationOccupationDetails;
import com.matrimony.model.entity.FamilyDetails;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ProfilePreference;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.DietaryHabits;
import com.matrimony.model.enums.DrinkingHabits;
import com.matrimony.model.enums.Gender;
import com.matrimony.model.enums.MaritalStatus;
import com.matrimony.model.enums.SmokingHabits;
import com.matrimony.repository.ConnectionRepository;
import com.matrimony.repository.ProfilePreferenceRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.UserService;

@Service
@Transactional
public class MatchService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ConnectionRepository connectionRepository;

	@Autowired
	private ProfilePreferenceRepository preferenceRepository;

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

	// Main method to get suggested profiles for logged-in user
//	public ResponseEntity getSuggestedProfilesForCurrentUser(int page, int size) {
//		try {
//			String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//			Long userId = getCurrentUserId();
//
//			Profile userProfile = profileRepository.findByUserId(userId).orElse(null);
//			if (userProfile == null) {
//				return new ResponseEntity("Profile not found for user: " + username, HttpStatus.NOT_FOUND.value(),
//						null);
//			}
//
//			// 3. Check if profile is complete
//			if (!userProfile.getProfileComplete()) {
//				Map<String, Object> response = new HashMap<>();
//				response.put("profileComplete", false);
//				response.put("completionPercentage", userProfile.getProfileCompletionPercentage());
//				response.put("message", "Complete your profile to see suggested matches");
//				return new ResponseEntity("Profile incomplete", 200, response);
//			}
//
//			// 4. Get user's preferences
//			ProfilePreference userPreferences = preferenceRepository.findByProfileId(userProfile.getId())
//					.orElseThrow(() -> new RuntimeException("Please set your match preferences to see suggestions"));
//
//			Gender preferredGender = userPreferences.getGender();
//
//			// 5. Get potential profiles (exclude current user, only complete profiles)
//			Pageable pageable = PageRequest.of(page, size);
//			Page<Profile> potentialProfiles = profileRepository.findByUserIdNotAndProfileCompleteTrueAndGender(userId,
//					preferredGender, pageable);
//
//			System.out.println("Found " + potentialProfiles.getTotalElements() + " potential profiles");
//
//			// 6. Calculate match scores and filter
//			List<MatchResult> suggestedMatches = potentialProfiles.getContent().stream().map(profile -> {
//				try {
//					return calculateMatchScore(userProfile, userPreferences, profile);
//				} catch (Exception e) {
//					System.err
//							.println("Error calculating match for profile " + profile.getId() + ": " + e.getMessage());
//					return null;
//				}
//			}).filter(Objects::nonNull).filter(result -> result.getMatchPercentage() > 40) // Show matches above 40%
//					.sorted((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()))
//					.collect(Collectors.toList());
//
//			System.out.println("Filtered to " + suggestedMatches.size() + " suggested matches");
//
//			// 7. Prepare response
//			Map<String, Object> response = new HashMap<>();
//			response.put("suggestedMatches", suggestedMatches);
//			response.put("totalSuggestions", suggestedMatches.size());
//			response.put("currentPage", page);
//			response.put("totalPages", potentialProfiles.getTotalPages());
//			response.put("userProfileId", userProfile.getId());
//			response.put("userPreferences", getPreferenceSummary(userPreferences));
//
//			String message = suggestedMatches.isEmpty()
//					? "No matches found yet. Try adjusting your preferences or check back later."
//					: "Found " + suggestedMatches.size() + " suggested matches for you";
//
//			response.put("message", message);
//
//			return new ResponseEntity(message, 200, response);
//
//		} catch (RuntimeException e) {
//			return new ResponseEntity(e.getMessage(), 400, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity("Error retrieving suggestions: " + e.getMessage(), 500, null);
//		}
//	}

	public ResponseEntity getSuggestedProfilesForCurrentUser(int page, int size) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();

			Long userId = getCurrentUserId();

			Profile userProfile = profileRepository.findByUserId(userId).orElse(null);

			if (userProfile == null) {
				return new ResponseEntity("Profile not found for user: " + username, HttpStatus.NOT_FOUND.value(),
						null);
			}

			if (!userProfile.getProfileComplete()) {
				Map<String, Object> response = new HashMap<>();
				response.put("profileComplete", false);
				response.put("completionPercentage", userProfile.getProfileCompletionPercentage());
				response.put("message", "Complete your profile to see suggested matches");
				return new ResponseEntity("Profile incomplete", 200, response);
			}

			ProfilePreference userPreferences = preferenceRepository.findByProfileId(userProfile.getId())
					.orElseThrow(() -> new RuntimeException("Please set your match preferences to see suggestions"));

			Gender preferredGender = userPreferences.getGender();

			Set<Long> connectedUserIds = new HashSet<>(connectionRepository.findConnectedUserIds(userId));

			connectedUserIds.add(userId);

			Pageable pageable = PageRequest.of(page, size);

			Page<Profile> potentialProfiles = profileRepository.findByUserIdNotAndProfileCompleteTrueAndGender(userId,
					preferredGender, pageable);

			List<MatchResult> suggestedMatches = potentialProfiles.getContent().stream()

					.filter(profile -> !connectedUserIds.contains(profile.getUser().getId()))

					.map(profile -> {
						try {
							return calculateMatchScore(userProfile, userPreferences, profile);
						} catch (Exception e) {
							System.err.println(
									"Error calculating match for profile " + profile.getId() + ": " + e.getMessage());
							return null;
						}
					}).filter(Objects::nonNull).filter(r -> r.getMatchPercentage() > 40)
					.sorted((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()))
					.collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("suggestedMatches", suggestedMatches);
			response.put("totalSuggestions", suggestedMatches.size());
			response.put("currentPage", page);
			response.put("totalPages", potentialProfiles.getTotalPages());
			response.put("userProfileId", userProfile.getId());
			response.put("userPreferences", getPreferenceSummary(userPreferences));

			String message = suggestedMatches.isEmpty()
					? "No matches found yet. Try adjusting your preferences or check back later."
					: "Found " + suggestedMatches.size() + " suggested matches for you";

			response.put("message", message);

			return new ResponseEntity(message, 200, response);

		} catch (RuntimeException e) {
			return new ResponseEntity(e.getMessage(), 400, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error retrieving suggestions: " + e.getMessage(), 500, null);
		}
	}

	public ResponseEntity getPendingMatches(int page, int size) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();

			Long userId = getCurrentUserId();

			Profile userProfile = profileRepository.findByUserId(userId).orElse(null);

			if (userProfile == null) {
				return new ResponseEntity("Profile not found for user: " + username, HttpStatus.NOT_FOUND.value(),
						null);
			}

			if (!userProfile.getProfileComplete()) {
				Map<String, Object> response = new HashMap<>();
				response.put("profileComplete", false);
				response.put("completionPercentage", userProfile.getProfileCompletionPercentage());
				response.put("message", "Complete your profile to see suggested matches");
				return new ResponseEntity("Profile incomplete", 200, response);
			}

			ProfilePreference userPreferences = preferenceRepository.findByProfileId(userProfile.getId())
					.orElseThrow(() -> new RuntimeException("Please set your match preferences to see suggestions"));

			Gender preferredGender = userPreferences.getGender();

			Set<Long> connectedUserIds = new HashSet<>(connectionRepository.findPendingSentUserIds(userId));

			connectedUserIds.add(userId);

			Pageable pageable = PageRequest.of(page, size);

			Page<Profile> potentialProfiles = profileRepository.findByUserIdNotAndProfileCompleteTrueAndGender(userId,
					preferredGender, pageable);

			List<MatchResult> suggestedMatches = potentialProfiles.getContent().stream()

					.filter(profile -> connectedUserIds.contains(profile.getUser().getId()))

					.map(profile -> {
						try {
							return calculateMatchScore(userProfile, userPreferences, profile);
						} catch (Exception e) {
							System.err.println(
									"Error calculating match for profile " + profile.getId() + ": " + e.getMessage());
							return null;
						}
					}).filter(Objects::nonNull).filter(r -> r.getMatchPercentage() > 40)
					.sorted((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()))
					.collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("suggestedMatches", suggestedMatches);
			response.put("totalSuggestions", suggestedMatches.size());
			response.put("currentPage", page);
			response.put("totalPages", potentialProfiles.getTotalPages());
			response.put("userProfileId", userProfile.getId());
			response.put("userPreferences", getPreferenceSummary(userPreferences));

			String message = suggestedMatches.isEmpty()
					? "No matches found yet. Try adjusting your preferences or check back later."
					: "Found " + suggestedMatches.size() + " suggested matches for you";

			response.put("message", message);

			return new ResponseEntity(message, 200, response);

		} catch (RuntimeException e) {
			return new ResponseEntity(e.getMessage(), 400, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error retrieving suggestions: " + e.getMessage(), 500, null);
		}
	}

	public ResponseEntity getInvitations(int page, int size) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();

			Long userId = getCurrentUserId();

			Profile userProfile = profileRepository.findByUserId(userId).orElse(null);

			if (userProfile == null) {
				return new ResponseEntity("Profile not found for user: " + username, HttpStatus.NOT_FOUND.value(),
						null);
			}

			if (!userProfile.getProfileComplete()) {
				Map<String, Object> response = new HashMap<>();
				response.put("profileComplete", false);
				response.put("completionPercentage", userProfile.getProfileCompletionPercentage());
				response.put("message", "Complete your profile to see suggested matches");
				return new ResponseEntity("Profile incomplete", 200, response);
			}

			ProfilePreference userPreferences = preferenceRepository.findByProfileId(userProfile.getId())
					.orElseThrow(() -> new RuntimeException("Please set your match preferences to see suggestions"));

			Gender preferredGender = userPreferences.getGender();

			Set<Long> senderUserIds = new HashSet<>(connectionRepository.findPendingReceivedUserIds(userId));

			senderUserIds.add(userId);

			Pageable pageable = PageRequest.of(page, size);

			Page<Profile> potentialProfiles = profileRepository.findByUserIdNotAndProfileCompleteTrueAndGender(userId,
					preferredGender, pageable);

			List<MatchResult> suggestedMatches = potentialProfiles.getContent().stream()

					.filter(profile -> senderUserIds.contains(profile.getUser().getId()))

					.map(profile -> {
						try {
							return calculateMatchScore(userProfile, userPreferences, profile);
						} catch (Exception e) {
							System.err.println(
									"Error calculating match for profile " + profile.getId() + ": " + e.getMessage());
							return null;
						}
					}).filter(Objects::nonNull).filter(r -> r.getMatchPercentage() > 40)
					.sorted((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()))
					.collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("suggestedMatches", suggestedMatches);
			response.put("totalSuggestions", suggestedMatches.size());
			response.put("currentPage", page);
			response.put("totalPages", potentialProfiles.getTotalPages());
			response.put("userProfileId", userProfile.getId());
			response.put("userPreferences", getPreferenceSummary(userPreferences));

			String message = suggestedMatches.isEmpty()
					? "No matches found yet. Try adjusting your preferences or check back later."
					: "Found " + suggestedMatches.size() + " suggested matches for you";

			response.put("message", message);

			return new ResponseEntity(message, 200, response);

		} catch (RuntimeException e) {
			return new ResponseEntity(e.getMessage(), 400, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error retrieving suggestions: " + e.getMessage(), 500, null);
		}
	}

	public ResponseEntity getAcceptedMatches(int page, int size) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();

			Long userId = getCurrentUserId();

			Profile userProfile = profileRepository.findByUserId(userId).orElse(null);

			if (userProfile == null) {
				return new ResponseEntity("Profile not found for user: " + username, HttpStatus.NOT_FOUND.value(),
						null);
			}

			if (!userProfile.getProfileComplete()) {
				Map<String, Object> response = new HashMap<>();
				response.put("profileComplete", false);
				response.put("completionPercentage", userProfile.getProfileCompletionPercentage());
				response.put("message", "Complete your profile to see suggested matches");
				return new ResponseEntity("Profile incomplete", 200, response);
			}

			ProfilePreference userPreferences = preferenceRepository.findByProfileId(userProfile.getId())
					.orElseThrow(() -> new RuntimeException("Please set your match preferences to see suggestions"));

			Gender preferredGender = userPreferences.getGender();

			Set<Long> acceptedUserIds = new HashSet<>(connectionRepository.findAcceptedConnectedUserIds(userId));

			Pageable pageable = PageRequest.of(page, size);

			Page<Profile> potentialProfiles = profileRepository.findByUserIdNotAndProfileCompleteTrueAndGender(userId,
					preferredGender, pageable);

			List<MatchResult> suggestedMatches = potentialProfiles.getContent().stream()

					.filter(profile -> acceptedUserIds.contains(profile.getUser().getId()))

					.map(profile -> {
						try {
							return calculateMatchScore(userProfile, userPreferences, profile);
						} catch (Exception e) {
							System.err.println(
									"Error calculating match for profile " + profile.getId() + ": " + e.getMessage());
							return null;
						}
					}).filter(Objects::nonNull).filter(r -> r.getMatchPercentage() > 40)
					.sorted((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()))
					.collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("suggestedMatches", suggestedMatches);
			response.put("totalSuggestions", suggestedMatches.size());
			response.put("currentPage", page);
			response.put("totalPages", potentialProfiles.getTotalPages());
			response.put("userProfileId", userProfile.getId());
			response.put("userPreferences", getPreferenceSummary(userPreferences));

			String message = suggestedMatches.isEmpty()
					? "No matches found yet. Try adjusting your preferences or check back later."
					: "Found " + suggestedMatches.size() + " suggested matches for you";

			response.put("message", message);

			return new ResponseEntity(message, 200, response);

		} catch (RuntimeException e) {
			return new ResponseEntity(e.getMessage(), 400, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error retrieving suggestions: " + e.getMessage(), 500, null);
		}
	}

	public ResponseEntity getFilteredProfiles(ProfileFilterRequest filterRequest) {
		try {
			Long userId = getCurrentUserId();
			Profile userProfile = profileRepository.findByUserId(userId).orElse(null);
			if (userProfile == null) {
				return new ResponseEntity("Profile not found for user: " + userId, HttpStatus.NOT_FOUND.value(), null);
			}

			if (!userProfile.getProfileComplete()) {
				Map<String, Object> response = new HashMap<>();
				response.put("profileComplete", false);
				response.put("completionPercentage", userProfile.getProfileCompletionPercentage());
				response.put("message", "Complete your profile to see filtered matches");
				return new ResponseEntity("Profile incomplete", 200, response);
			}

			ProfilePreference userPreferences = preferenceRepository.findByProfileId(userProfile.getId())
					.orElseThrow(() -> new RuntimeException("Please set your match preferences to see suggestions"));

//			Gender genderToFilter = filterRequest.getGender() != null
//					? Gender.valueOf(filterRequest.getGender().toUpperCase())
//					: userPreferences.getGender();
			
			Gender genderToFilter;

			if (filterRequest.getGender() != null) {
			    Gender requestedGender = Gender.valueOf(filterRequest.getGender().toUpperCase());
			    if (requestedGender == userProfile.getGender()) {
			        genderToFilter = getOppositeGender(userProfile.getGender());
			    } else {
			        genderToFilter = requestedGender;
			    }
			} else {
			    genderToFilter = getOppositeGender(userProfile.getGender());
			}


			Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());

			Page<Profile> filteredProfiles = getFilteredProfiles(userId, genderToFilter, filterRequest, pageable);

			List<MatchResult> suggestedMatches = filteredProfiles.getContent().stream().map(profile -> {
				try {
					return calculateMatchScore(userProfile, userPreferences, profile);
				} catch (Exception e) {
					System.err
							.println("Error calculating match for profile " + profile.getId() + ": " + e.getMessage());
					return null;
				}
			}).filter(Objects::nonNull).filter(result -> result.getMatchPercentage() > 40) // Show matches above 40%
					.sorted((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()))
					.collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("suggestedMatches", suggestedMatches);
			response.put("totalSuggestions", suggestedMatches.size());
			response.put("currentPage", filterRequest.getPage());
			response.put("totalPages", filteredProfiles.getTotalPages());
			response.put("userProfileId", userProfile.getId());
			response.put("userPreferences", getPreferenceSummary(userPreferences));

			response.put("appliedFilters", getAppliedFiltersSummary(filterRequest));

			String message = suggestedMatches.isEmpty()
					? "No matches found with the applied filters. Try adjusting your criteria."
					: "Found " + suggestedMatches.size() + " matches with applied filters";

			response.put("message", message);

			return new ResponseEntity(message, 200, response);

		} catch (RuntimeException e) {
			return new ResponseEntity(e.getMessage(), 400, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error retrieving filtered profiles: " + e.getMessage(), 500, null);
		}
	}
	
	private Gender getOppositeGender(Gender gender) {
	    return gender == Gender.MALE ? Gender.FEMALE : Gender.MALE;
	}


	private Page<Profile> getFilteredProfiles(Long currentUserId, Gender gender, ProfileFilterRequest filterRequest,
			Pageable pageable) {
		return profileRepository.findFilteredProfiles(currentUserId, gender, filterRequest, pageable);
	}

	private Map<String, Object> getAppliedFiltersSummary(ProfileFilterRequest filterRequest) {
		Map<String, Object> summary = new HashMap<>();

		// Basic Preferences
		if (filterRequest.getGender() != null) {
			summary.put("gender", filterRequest.getGender());
		}
		if (filterRequest.getMinAge() != null || filterRequest.getMaxAge() != null) {
			summary.put("ageRange", filterRequest.getMinAge() + "-" + filterRequest.getMaxAge());
		}
		if (filterRequest.getMaritalStatus() != null) {
			summary.put("maritalStatus", filterRequest.getMaritalStatus());
		}

		// Religion
		if (filterRequest.getReligion() != null) {
			summary.put("religion", filterRequest.getReligion());
		}
		if (filterRequest.getCastes() != null && !filterRequest.getCastes().isEmpty()) {
			summary.put("castes", filterRequest.getCastes());
		}

		return summary;
	}

	// Get top matches for user dashboard (after login)
	public ResponseEntity getDashboardSuggestions() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = getCurrentUserId();
			Profile userProfile = profileRepository.findByUserId(userId).orElse(null);
			if (userProfile == null) {
				return new ResponseEntity("Profile not found for user: " + username, HttpStatus.NOT_FOUND.value(),
						null);
			}

			if (!userProfile.getProfileComplete()) {
				Map<String, Object> response = new HashMap<>();
				response.put("profileComplete", false);
				response.put("completionPercentage", userProfile.getProfileCompletionPercentage());
				response.put("nextStep", "Complete your profile to see matches");
				return new ResponseEntity("Profile incomplete", 200, response);
			}

			ProfilePreference userPreferences = preferenceRepository.findByProfileId(userProfile.getId())
					.orElseThrow(() -> new RuntimeException("Please set your preferences"));

			Gender preferredGender = userPreferences.getGender();

			// Get top 8 high-quality matches for dashboard
			Pageable pageable = PageRequest.of(0, 20); // Get more to filter
			Page<Profile> potentialProfiles = profileRepository.findByUserIdNotAndProfileCompleteTrueAndGender(userId,
					preferredGender, pageable);

			List<MatchResult> dashboardMatches = potentialProfiles.getContent().stream()
					.map(profile -> calculateMatchScore(userProfile, userPreferences, profile))
					.filter(result -> result.getMatchPercentage() > 60)
					.sorted((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage())).limit(8)

					.collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("dashboardMatches", dashboardMatches);
			response.put("userName", userProfile.getFirstName());
			response.put("profileCompletion", userProfile.getProfileCompletionPercentage());
			response.put("totalMatches", dashboardMatches.size());
			response.put("welcomeMessage", "Welcome back! Here are your top matches");

			return new ResponseEntity("Dashboard suggestions loaded", 200, response);

		} catch (RuntimeException e) {
			return new ResponseEntity(e.getMessage(), 400, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error loading dashboard: " + e.getMessage(), 500, null);
		}
	}

	// Get quick matches (top 5 for quick view)
	public ResponseEntity getQuickSuggestions() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = getCurrentUserId();
			Profile userProfile = profileRepository.findByUserId(userId).orElse(null);
			if (userProfile == null) {
				return new ResponseEntity("Profile not found for user: " + username, HttpStatus.NOT_FOUND.value(),
						null);
			}

			ProfilePreference userPreferences = preferenceRepository.findByProfileId(userProfile.getId())
					.orElseThrow(() -> new RuntimeException("Preferences not found"));
			Gender preferredGender = userPreferences.getGender();

			Pageable pageable = PageRequest.of(0, 15);
			Page<Profile> potentialProfiles = profileRepository.findByUserIdNotAndProfileCompleteTrueAndGender(userId,
					preferredGender, pageable);

			List<MatchResult> quickMatches = potentialProfiles.getContent().stream()
					.map(profile -> calculateMatchScore(userProfile, userPreferences, profile))
					.filter(result -> result.getMatchPercentage() > 50)
					.sorted((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage())).limit(5)
					.collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("quickMatches", quickMatches);
			response.put("count", quickMatches.size());

			return new ResponseEntity("Quick matches loaded", 200, response);

		} catch (Exception e) {
			return new ResponseEntity("Error loading quick matches: " + e.getMessage(), 500, null);
		}
	}

	private MatchResult calculateMatchScore(Profile userProfile, ProfilePreference userPreference,
			Profile potentialProfile) {
		double totalScore = 0.0;
		double maxPossibleScore = 0.0;
		Map<String, Double> categoryScores = new HashMap<>();
		List<String> matchReasons = new ArrayList<>();

		// Basic Information Matching (25%)
		double basicScore = calculateBasicInfoScore(userPreference, potentialProfile);
		totalScore += basicScore;
		maxPossibleScore += 25.0;
		categoryScores.put("basic", (basicScore / 25.0) * 100);

		// Religious & Cultural Matching (25%)
		double religiousScore = calculateReligiousCulturalScore(userPreference, potentialProfile);
		totalScore += religiousScore;
		maxPossibleScore += 25.0;
		categoryScores.put("religious", (religiousScore / 25.0) * 100);

		// Professional & Educational Matching (20%)
		double professionalScore = calculateProfessionalEducationalScore(userPreference, potentialProfile);
		totalScore += professionalScore;
		maxPossibleScore += 20.0;
		categoryScores.put("professional", (professionalScore / 20.0) * 100);

		// Lifestyle Matching (15%)
		double lifestyleScore = calculateLifestyleScore(userPreference, potentialProfile);
		totalScore += lifestyleScore;
		maxPossibleScore += 15.0;
		categoryScores.put("lifestyle", (lifestyleScore / 15.0) * 100);

		// Family Background Matching (15%)
		double familyScore = calculateFamilyBackgroundScore(userProfile, potentialProfile);
		totalScore += familyScore;
		maxPossibleScore += 15.0;
		categoryScores.put("family", (familyScore / 15.0) * 100);

		double matchPercentage = maxPossibleScore > 0 ? (totalScore / maxPossibleScore) * 100 : 0;

		// Get match reasons
		matchReasons.addAll(getMatchReasons(userPreference, potentialProfile));

		return new MatchResult(potentialProfile, matchPercentage, matchReasons, categoryScores);
	}

	private double calculateBasicInfoScore(ProfilePreference preference, Profile potentialProfile) {
		double score = 0.0;

		// Age matching (5 points)
		if (preference.getMinAge() != null && preference.getMaxAge() != null) {
			int age = calculateAge(potentialProfile.getDateOfBirth());
			if (age >= preference.getMinAge() && age <= preference.getMaxAge()) {
				score += 5.0;
			}
		}

		// Gender matching (5 points)
		if (preference.getGender() != null && preference.getGender() == potentialProfile.getGender()) {
			score += 5.0;
		}

		// Height matching (5 points)
		if (preference.getMinHeight() != null && preference.getMaxHeight() != null
				&& potentialProfile.getHeightIn() != null) {
			if (isHeightInRange(potentialProfile.getHeightIn(), preference.getMinHeight(), preference.getMaxHeight())) {
				score += 5.0;
			}
		}

		// Marital status matching (5 points)
		if (preference.getMaritalStatus() != null) {
			if (preference.getMaritalStatus() == MaritalStatus.DOESNT_MATTER
					|| preference.getMaritalStatus() == potentialProfile.getMaritalStatus()) {
				score += 5.0;
			}
		}

		// Physical status matching (5 points)
		if (preference.getPhysicalStatus() != null) {
			if (preference.getPhysicalStatus() == potentialProfile.getPhysicalStatus()) {
				score += 5.0;
			}
		}

		return score;
	}

	private double calculateReligiousCulturalScore(ProfilePreference preference, Profile potentialProfile) {
		double score = 0.0;

		// Religion matching (7 points)
		if (preference.getReligion() != null) {
			if (preference.getReligion() == potentialProfile.getReligion()) {
				score += 7.0;
			}
		}

		// Caste matching (6 points)
		if (preference.getCastes() != null && !preference.getCastes().isEmpty()) {
			if (preference.getCastes().contains(potentialProfile.getCaste())) {
				score += 6.0;
			}
		}

		// Sub-caste matching (5 points)
		if (preference.getSubCastes() != null && !preference.getSubCastes().isEmpty()) {
			if (preference.getSubCastes().contains(potentialProfile.getSubCaste())) {
				score += 5.0;
			}
		}

		// Mother tongue matching (4 points)
		if (preference.getMotherTongues() != null && !preference.getMotherTongues().isEmpty()) {
			if (preference.getMotherTongues().contains(potentialProfile.getMotherTongue())) {
				score += 4.0;
			}
		}

		// Gothra matching (3 points)
		if (preference.getGothras() != null && !preference.getGothras().isEmpty()
				&& potentialProfile.getGothra() != null) {
			if (preference.getGothras().contains(potentialProfile.getGothra())) {
				score += 3.0;
			}
		}

		return score;
	}

	private double calculateProfessionalEducationalScore(ProfilePreference preference, Profile potentialProfile) {
		double score = 0.0;
		EducationOccupationDetails eduOcc = potentialProfile.getEducationOccupationDetails();

		if (eduOcc != null) {
			// Education level matching (6 points)
			if (preference.getEducationLevels() != null && !preference.getEducationLevels().isEmpty()
					&& eduOcc.getHighestEducation() != null) {
				if (preference.getEducationLevels().contains(eduOcc.getHighestEducation())) {
					score += 6.0;
				}
			}

			// Employment type matching (5 points)
			if (preference.getEmployedIn() != null && !preference.getEmployedIn().isEmpty()
					&& eduOcc.getEmployedIn() != null) {
				if (preference.getEmployedIn().contains(eduOcc.getEmployedIn().name())) {
					score += 5.0;
				}
			}

			// Occupation matching (5 points)
			if (preference.getOccupations() != null && !preference.getOccupations().isEmpty()
					&& eduOcc.getOccupation() != null) {
				if (preference.getOccupations().contains(eduOcc.getOccupation())) {
					score += 5.0;
				}
			}

			// Income matching (4 points)
			if (preference.getAnnualIncome() != null && eduOcc.getAnnualIncome() != null) {
				if (isIncomeInRange(eduOcc.getAnnualIncome(), preference.getAnnualIncome())) {
					score += 4.0;
				}
			}
		}

		return score;
	}

	private double calculateLifestyleScore(ProfilePreference preference, Profile potentialProfile) {
		double score = 0.0;

		// Dietary habits matching (5 points)
		if (preference.getDietaryHabits() != null) {
			if (preference.getDietaryHabits() == DietaryHabits.DOESNT_MATTER
					|| preference.getDietaryHabits() == potentialProfile.getDietaryHabits()) {
				score += 5.0;
			}
		}

		// Smoking habits matching (5 points)
		if (preference.getSmokingHabits() != null) {
			if (preference.getSmokingHabits() == SmokingHabits.DOESNT_MATTER
					|| preference.getSmokingHabits() == potentialProfile.getSmokingHabits()) {
				score += 5.0;
			}
		}

		// Drinking habits matching (5 points)
		if (preference.getDrinkingHabits() != null) {
			if (preference.getDrinkingHabits() == DrinkingHabits.DOESNT_MATTER
					|| preference.getDrinkingHabits() == potentialProfile.getDrinkingHabits()) {
				score += 5.0;
			}
		}

		return score;
	}

	private double calculateFamilyBackgroundScore(Profile userProfile, Profile potentialProfile) {
		double score = 0.0;
		FamilyDetails userFamily = userProfile.getFamilyDetails();
		FamilyDetails potentialFamily = potentialProfile.getFamilyDetails();

		if (userFamily != null && potentialFamily != null) {
			// Family value matching (4 points)
			if (userFamily.getFamilyValue() != null && potentialFamily.getFamilyValue() != null) {
				if (userFamily.getFamilyValue() == potentialFamily.getFamilyValue()) {
					score += 4.0;
				}
			}

			// Family type matching (3 points)
			if (userFamily.getFamilyType() != null && potentialFamily.getFamilyType() != null) {
				if (userFamily.getFamilyType() == potentialFamily.getFamilyType()) {
					score += 3.0;
				}
			}

			// Family status matching (4 points)
			if (userFamily.getFamilyStatus() != null && potentialFamily.getFamilyStatus() != null) {
				if (userFamily.getFamilyStatus() == potentialFamily.getFamilyStatus()) {
					score += 4.0;
				}
			}

			// Location compatibility (4 points)
			if (userProfile.getLocation() != null && potentialProfile.getLocation() != null) {
				if (userProfile.getLocation().getCountry() != null
						&& potentialProfile.getLocation().getCountry() != null
						&& userProfile.getLocation().getCountry().equals(potentialProfile.getLocation().getCountry())) {
					score += 4.0;
				}
			}
		}

		return score;
	}

	private List<String> getMatchReasons(ProfilePreference preference, Profile potentialProfile) {
		List<String> reasons = new ArrayList<>();

		if (preference.getReligion() != null && preference.getReligion() == potentialProfile.getReligion()) {
			reasons.add("Same religion");
		}

		if (preference.getCastes() != null && !preference.getCastes().isEmpty()
				&& preference.getCastes().contains(potentialProfile.getCaste())) {
			reasons.add("Caste preference matched");
		}

		EducationOccupationDetails eduOcc = potentialProfile.getEducationOccupationDetails();
		if (eduOcc != null && eduOcc.getHighestEducation() != null && preference.getEducationLevels() != null
				&& preference.getEducationLevels().contains(eduOcc.getHighestEducation())) {
			reasons.add("Education level matched");
		}

		if (preference.getDietaryHabits() != null
				&& preference.getDietaryHabits() == potentialProfile.getDietaryHabits()) {
			reasons.add("Similar dietary habits");
		}

		if (preference.getMinAge() != null && preference.getMaxAge() != null) {
			int age = calculateAge(potentialProfile.getDateOfBirth());
			if (age >= preference.getMinAge() && age <= preference.getMaxAge()) {
				reasons.add("Age within preferred range");
			}
		}

		return reasons.stream().limit(4).collect(Collectors.toList());
	}

	private Map<String, Object> getPreferenceSummary(ProfilePreference preferences) {
		Map<String, Object> summary = new HashMap<>();
		summary.put("preferredAge", preferences.getMinAge() + "-" + preferences.getMaxAge());
		summary.put("preferredGender", preferences.getGender());
		summary.put("preferredReligion", preferences.getReligion());
		summary.put("totalCastePreferences", preferences.getCastes() != null ? preferences.getCastes().size() : 0);
		return summary;
	}

	// Utility methods
	private int calculateAge(LocalDate dateOfBirth) {
		return Period.between(dateOfBirth, LocalDate.now()).getYears();
	}

	private boolean isHeightInRange(String height, String minHeight, String maxHeight) {
		try {
			double currentHeight = parseHeight(height);
			double min = parseHeight(minHeight);
			double max = parseHeight(maxHeight);
			return currentHeight >= min && currentHeight <= max;
		} catch (Exception e) {
			return false;
		}
	}

	private double parseHeight(String height) {
		try {
			return Double.parseDouble(height);
		} catch (NumberFormatException e) {
			if (height.contains("'")) {
				String[] parts = height.split("'");
				double feet = Double.parseDouble(parts[0]);
				double inches = parts.length > 1 ? Double.parseDouble(parts[1].replace("\"", "")) : 0;
				return feet + (inches / 12);
			}
			return 0;
		}
	}

	private boolean isIncomeInRange(String actualIncome, String preferredRange) {
		try {
			String cleanActual = actualIncome.replaceAll("[^0-9.]", "");
			double income = Double.parseDouble(cleanActual);

			if (preferredRange.contains("-")) {
				String[] range = preferredRange.split("-");
				double min = Double.parseDouble(range[0].replaceAll("[^0-9.]", ""));
				double max = Double.parseDouble(range[1].replaceAll("[^0-9.]", ""));
				return income >= min && income <= max;
			} else if (preferredRange.contains("+")) {
				double min = Double.parseDouble(preferredRange.replaceAll("[^0-9.]", ""));
				return income >= min;
			} else {
				double expected = Double.parseDouble(preferredRange.replaceAll("[^0-9.]", ""));
				return income >= expected * 0.8 && income <= expected * 1.2;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// Add this method to your MatchService class
	public ResponseEntity getMatchPercentage(Long otherProfileId) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User currentUser = userService.getUserByUsername(username);

			// Get current user's profile
			Profile currentUserProfile = profileRepository.findByUserId(currentUser.getId())
					.orElseThrow(() -> new RuntimeException("Your profile not found. Please complete your profile."));

			// Check if current user's profile is complete
			if (!currentUserProfile.getProfileComplete()) {
				Map<String, Object> response = new HashMap<>();
				response.put("profileComplete", false);
				response.put("completionPercentage", currentUserProfile.getProfileCompletionPercentage());
				response.put("message", "Please complete your profile to calculate match percentage");
				return new ResponseEntity("Profile incomplete", 400, response);
			}

			// Get current user's preferences
			ProfilePreference currentUserPreferences = preferenceRepository.findByProfileId(currentUserProfile.getId())
					.orElseThrow(() -> new RuntimeException("Please set your match preferences first"));

			// Get the other profile
			Profile otherProfile = profileRepository.findById(otherProfileId)
					.orElseThrow(() -> new RuntimeException("Profile not found with ID: " + otherProfileId));

			// Check if other profile is complete
			if (!otherProfile.getProfileComplete()) {
				return new ResponseEntity("The requested profile is not complete", 400, null);
			}

			// Prevent self-match calculation
			if (currentUserProfile.getId().equals(otherProfile.getId())) {
				Map<String, Object> response = new HashMap<>();
				response.put("error", "Cannot calculate match with yourself");
				response.put("message", "Try checking match percentage with other profiles");
				return new ResponseEntity("Self-match not allowed", 400, response);
			}

			// Calculate match score
			MatchResult matchResult = calculateMatchScore(currentUserProfile, currentUserPreferences, otherProfile);

			// Prepare detailed response
			Map<String, Object> response = createMatchPercentageResponse(currentUserProfile, otherProfile, matchResult);

			return new ResponseEntity("Match percentage calculated successfully", 200, response);

		} catch (RuntimeException e) {
			return new ResponseEntity(e.getMessage(), 404, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error calculating match percentage: " + e.getMessage(), 500, null);
		}
	}

	private Map<String, Object> createMatchPercentageResponse(Profile currentUserProfile, Profile otherProfile,
			MatchResult matchResult) {
		Map<String, Object> response = new HashMap<>();

		// Basic match info
		response.put("matchPercentage", Math.round(matchResult.getMatchPercentage() * 100.0) / 100.0); // Round to 2
																										// decimal
		response.put("compatibilityLevel", getCompatibilityLevel(matchResult.getMatchPercentage()));
		response.put("matchReasons", matchResult.getMatchReasons());

		// Profile information
		response.put("yourProfile", getProfileSummary(currentUserProfile));
		response.put("theirProfile", getProfileSummary(otherProfile));

		// Detailed scores
		response.put("categoryScores", matchResult.getCategoryScores());
		response.put("totalScore", calculateTotalScore(matchResult.getCategoryScores()));

		// Suggestions for improvement (if match is low)
		if (matchResult.getMatchPercentage() < 60) {
			response.put("improvementSuggestions", getImprovementSuggestions(matchResult.getCategoryScores()));
		}

		return response;
	}

	private Map<String, Object> getProfileSummary(Profile profile) {
		Map<String, Object> summary = new HashMap<>();
		summary.put("id", profile.getId());
		summary.put("name", profile.getFirstName() + " " + profile.getLastName());
		summary.put("age", calculateAge(profile.getDateOfBirth()));
		summary.put("gender", profile.getGender());
		summary.put("religion", profile.getReligion());
		summary.put("caste", profile.getCaste());
		summary.put("education", getEducationSummary(profile));
		summary.put("profession", getProfessionSummary(profile));
		summary.put("location", getLocationSummary(profile));
		return summary;
	}

	private String getEducationSummary(Profile profile) {
		if (profile.getEducationOccupationDetails() != null) {
			return profile.getEducationOccupationDetails().getHighestEducation();
		}
		return "Not specified";
	}

	private String getProfessionSummary(Profile profile) {
		if (profile.getEducationOccupationDetails() != null) {
			String occupation = profile.getEducationOccupationDetails().getOccupation();
			return occupation != null ? occupation : "Not specified";
		}
		return "Not specified";
	}

	private String getLocationSummary(Profile profile) {
		if (profile.getLocation() != null) {
			String city = profile.getLocation().getCity();
			String country = profile.getLocation().getCountry();
			if (city != null && country != null) {
				return city + ", " + country;
			} else if (city != null) {
				return city;
			} else if (country != null) {
				return country;
			}
		}
		return "Not specified";
	}

	private double calculateTotalScore(Map<String, Double> categoryScores) {
		return categoryScores.values().stream().mapToDouble(Double::doubleValue).sum();
	}

	private List<String> getImprovementSuggestions(Map<String, Double> categoryScores) {
		List<String> suggestions = new ArrayList<>();

		if (categoryScores.getOrDefault("basic", 0.0) < 60) {
			suggestions.add("Consider adjusting your age, height, or marital status preferences");
		}
		if (categoryScores.getOrDefault("religious", 0.0) < 60) {
			suggestions.add("You might want to be more flexible with religious preferences");
		}
		if (categoryScores.getOrDefault("professional", 0.0) < 60) {
			suggestions.add("Consider broadening your education or profession preferences");
		}
		if (categoryScores.getOrDefault("lifestyle", 0.0) < 60) {
			suggestions.add("Dietary or habit preferences might be limiting your matches");
		}
		if (categoryScores.getOrDefault("family", 0.0) < 60) {
			suggestions.add("Family background preferences could be adjusted for better matches");
		}

		return suggestions;
	}

	private String getCompatibilityLevel(double matchPercentage) {
		if (matchPercentage >= 90)
			return "EXCELLENT";
		if (matchPercentage >= 80)
			return "VERY_GOOD";
		if (matchPercentage >= 70)
			return "GOOD";
		if (matchPercentage >= 60)
			return "FAIR";
		if (matchPercentage >= 50)
			return "AVERAGE";
		if (matchPercentage >= 40)
			return "BELOW_AVERAGE";
		return "LOW";
	}
}