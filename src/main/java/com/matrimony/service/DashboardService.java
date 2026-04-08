package com.matrimony.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.matrimony.model.entity.Profile;
import com.matrimony.model.enums.Gender;
import com.matrimony.model.enums.HelpRequestStatus;
import com.matrimony.model.enums.PaymentStatus;
import com.matrimony.model.enums.ProfileVerificationStatus;
import com.matrimony.model.enums.ReportStatus;
import com.matrimony.model.enums.SubscriptionStatus;
import com.matrimony.repository.HelpRequestRepository;
import com.matrimony.repository.LocationRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.repository.RazorpayPaymentRepository;
import com.matrimony.repository.ReportRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.repository.UserSubscriptionRepository;

@Service
public class DashboardService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;

	@Autowired
	private RazorpayPaymentRepository razorpayPaymentRepository;

	@Autowired
	private HelpRequestRepository helpRequestRepository;

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private LocationRepository locationRepository;

	public Map<String, Object> getDashboardData(LocalDateTime fromDate, LocalDateTime toDate) {
		Map<String, Object> payload = new HashMap<>();

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime todayStart = now.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime todayEnd = now.withHour(23).withMinute(59).withSecond(59);

		// Apply date range filters
		LocalDateTime userFromDate = fromDate != null ? fromDate : null;
		LocalDateTime userToDate = toDate != null ? toDate : null;

		LocalDateTime paymentFromDate = fromDate != null ? fromDate : null;
		LocalDateTime paymentToDate = toDate != null ? toDate : null;

		if (userFromDate != null || userToDate != null) {
			payload.put("totalUsers", userRepository.countByCreatedAtBetween(userFromDate, userToDate));
			payload.put("activeUsers", userRepository.countActiveUsersByDateRange(userFromDate, userToDate));
			payload.put("inactiveUsers", userRepository.countInactiveUsersByDateRange(userFromDate, userToDate));
			payload.put("newToday", userRepository.countByCreatedAtBetween(todayStart, todayEnd));

			payload.put("verifiedProfiles", profileRepository
					.countVerifiedProfilesByDateRange(ProfileVerificationStatus.VERIFIED, userFromDate, userToDate));

			payload.put("activeProfiles", profileRepository.countActiveProfilesByDateRange(userFromDate, userToDate));

			payload.put("suspendedProfiles", profileRepository
					.countSuspendedProfilesByDateRange(ProfileVerificationStatus.SUSPENDED, userFromDate, userToDate));

			payload.put("genderSplit", getGenderSplitByDateRange(userFromDate, userToDate));
			payload.put("monthlyRegistrations", getMonthlyRegistrations(userFromDate, userToDate));
		} else {
			payload.put("totalUsers", userRepository.count());
			payload.put("activeUsers", userRepository.countByIsActiveTrue());
			payload.put("inactiveUsers", userRepository.countByIsActiveFalse());
			payload.put("newToday", userRepository.countByCreatedAtBetween(todayStart, todayEnd));
			payload.put("verifiedProfiles",
					profileRepository.countByVerificationStatus(ProfileVerificationStatus.VERIFIED));
			payload.put("activeProfiles", profileRepository.countActiveProfiles());
			payload.put("suspendedProfiles",
					profileRepository.countByVerificationStatus(ProfileVerificationStatus.SUSPENDED));
			payload.put("genderSplit", getGenderSplit());
			payload.put("monthlyRegistrations", getMonthlyRegistrations(null, null));
		}

		payload.put("premiumMembers",
				userSubscriptionRepository.countDistinctUsersWithActiveSubscription(LocalDate.now()));
		payload.put("activeSubscriptions",
				userSubscriptionRepository.countByStatusAndEndDateAfter(SubscriptionStatus.ACTIVE, LocalDate.now()));

		if (paymentFromDate != null || paymentToDate != null) {
			BigDecimal totalRevenueBD = razorpayPaymentRepository
					.sumSuccessfulPaymentsByDateRange(PaymentStatus.CAPTURED, paymentFromDate, paymentToDate);
			payload.put("totalRevenue", totalRevenueBD != null ? totalRevenueBD.doubleValue() : 0.0);
		} else {
			BigDecimal totalRevenueBD = razorpayPaymentRepository.sumSuccessfulPayments(PaymentStatus.CAPTURED);
			payload.put("totalRevenue", totalRevenueBD != null ? totalRevenueBD.doubleValue() : 0.0);
		}

		if (userFromDate != null || userToDate != null) {
			Long totalHelpRequests = helpRequestRepository.countByCreatedAtBetween(userFromDate, userToDate);
			Long totalReports = reportRepository.countByCreatedAtBetween(userFromDate, userToDate);
			payload.put("totalComplaints", totalHelpRequests + totalReports);

			Long resolvedComplaints = helpRequestRepository.countByStatusAndCreatedAtBetween(HelpRequestStatus.RESOLVED,
					userFromDate, userToDate)
					+ reportRepository.countByStatusAndCreatedAtBetween(ReportStatus.RESOLVED, userFromDate,
							userToDate);
			payload.put("resolvedComplaints", resolvedComplaints);

			Long pendingComplaints = helpRequestRepository.countByStatusAndCreatedAtBetween(HelpRequestStatus.PENDING,
					userFromDate, userToDate)
					+ reportRepository.countByStatusAndCreatedAtBetween(ReportStatus.PENDING, userFromDate, userToDate);
			payload.put("pendingComplaints", pendingComplaints);
		} else {
			Long totalHelpRequests = helpRequestRepository.count();
			Long totalReports = reportRepository.count();
			payload.put("totalComplaints", totalHelpRequests + totalReports);

			Long resolvedComplaints = helpRequestRepository.countByStatus(HelpRequestStatus.RESOLVED)
					+ reportRepository.countByStatus(ReportStatus.RESOLVED);
			payload.put("resolvedComplaints", resolvedComplaints);

			Long pendingComplaints = helpRequestRepository.countByStatus(HelpRequestStatus.PENDING)
					+ reportRepository.countByStatus(ReportStatus.PENDING);
			payload.put("pendingComplaints", pendingComplaints);
		}

		return payload;
	}

	private Map<String, Long> getGenderSplit() {
		Map<String, Long> genderSplit = new HashMap<>();
		genderSplit.put("male", profileRepository.countByGender(Gender.MALE));
		genderSplit.put("female", profileRepository.countByGender(Gender.FEMALE));
		return genderSplit;
	}

	private Map<String, Long> getGenderSplitByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
		Map<String, Long> genderSplit = new HashMap<>();
		genderSplit.put("male", profileRepository.countByGenderAndDateRange(Gender.MALE, fromDate, toDate));
		genderSplit.put("female", profileRepository.countByGenderAndDateRange(Gender.FEMALE, fromDate, toDate));
		return genderSplit;
	}

	private List<Map<String, Object>> getMonthlyRegistrations(LocalDateTime fromDate, LocalDateTime toDate) {
		List<Map<String, Object>> result = new ArrayList<>();

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate = fromDate != null ? fromDate : now.minusMonths(11);
		LocalDateTime endDate = toDate != null ? toDate : now;

		LocalDateTime current = startDate.withDayOfMonth(1);
		while (!current.isAfter(endDate)) {
			LocalDateTime monthStart = current.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
			LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);

			String monthName = monthStart.format(DateTimeFormatter.ofPattern("MMM"));
			Long registrations = userRepository.countByCreatedAtBetween(monthStart, monthEnd);

			Map<String, Object> monthData = new HashMap<>();
			monthData.put("month", monthName);
			monthData.put("registrations", registrations);
			result.add(monthData);

			current = current.plusMonths(1);
		}

		return result;
	}

	public Map<String, Object> getDateRangeDashboardData(LocalDateTime fromDate, LocalDateTime toDate) {
		Map<String, Object> payload = new LinkedHashMap<>();

		payload.put("totalRecords", getTotalProfilesCount(fromDate, toDate));

		payload.put("ageDistribution", getAgeDistribution(fromDate, toDate));

		payload.put("regionDistribution", getRegionDistribution(fromDate, toDate));

		payload.put("paymentTrend", getPaymentTrend(fromDate, toDate));

		return payload;
	}

	private int getTotalProfilesCount(LocalDateTime fromDate, LocalDateTime toDate) {
		if (fromDate != null && toDate != null) {
			Page<Profile> profiles = profileRepository.findByCreatedAtBetween(fromDate, toDate, PageRequest.of(0, 1));
			return (int) profiles.getTotalElements();
		} else if (fromDate != null) {
			Page<Profile> profiles = profileRepository.findByCreatedAtGreaterThanEqual(fromDate, PageRequest.of(0, 1));
			return (int) profiles.getTotalElements();
		} else if (toDate != null) {
			Page<Profile> profiles = profileRepository.findByCreatedAtLessThanEqual(toDate, PageRequest.of(0, 1));
			return (int) profiles.getTotalElements();
		}
		return (int) profileRepository.count();
	}

	private List<Map<String, Object>> getAgeDistribution(LocalDateTime fromDate, LocalDateTime toDate) {
		List<Map<String, Object>> ageDistribution = new ArrayList<>();

		List<Profile> profiles = getProfilesByDateRange(fromDate, toDate);

		int age18_22 = 0, age23_27 = 0, age28_32 = 0, age33_37 = 0, age38Plus = 0;
		LocalDate now = LocalDate.now();

		for (Profile profile : profiles) {
			LocalDate dob = profile.getDateOfBirth();
			if (dob != null) {
				int age = Period.between(dob, now).getYears();

				if (age >= 18 && age <= 22) {
					age18_22++;
				} else if (age >= 23 && age <= 27) {
					age23_27++;
				} else if (age >= 28 && age <= 32) {
					age28_32++;
				} else if (age >= 33 && age <= 37) {
					age33_37++;
				} else if (age >= 38) {
					age38Plus++;
				}
			}
		}

		ageDistribution.add(createAgeRangeMap("18-22", age18_22));
		ageDistribution.add(createAgeRangeMap("23-27", age23_27));
		ageDistribution.add(createAgeRangeMap("28-32", age28_32));
		ageDistribution.add(createAgeRangeMap("33-37", age33_37));
		ageDistribution.add(createAgeRangeMap("38+", age38Plus));

		return ageDistribution;
	}

	private Map<String, Object> createAgeRangeMap(String range, int count) {
		Map<String, Object> rangeMap = new LinkedHashMap<>();
		rangeMap.put("range", range);
		rangeMap.put("count", count);
		return rangeMap;
	}

	private List<Map<String, Object>> getRegionDistribution(LocalDateTime fromDate, LocalDateTime toDate) {
		List<Map<String, Object>> regionDistribution = new ArrayList<>();

		List<Object[]> results;
		if (fromDate != null && toDate != null) {
			results = locationRepository.countByStateAndProfileCreatedAtBetween(fromDate, toDate);
		} else if (fromDate != null) {
			results = locationRepository.countByStateAndProfileCreatedAtAfter(fromDate);
		} else if (toDate != null) {
			results = locationRepository.countByStateAndProfileCreatedAtBefore(toDate);
		} else {
			results = locationRepository.countByStateGrouped();
		}

		int upCount = 0, rajasthanCount = 0, mpCount = 0, biharCount = 0, othersCount = 0;

		for (Object[] result : results) {
			String state = (String) result[0];
			Long count = (Long) result[1];

			if (state != null) {
				if (state.equalsIgnoreCase("UP") || state.equalsIgnoreCase("Uttar Pradesh")) {
					upCount = count.intValue();
				} else if (state.equalsIgnoreCase("Rajasthan")) {
					rajasthanCount = count.intValue();
				} else if (state.equalsIgnoreCase("MP") || state.equalsIgnoreCase("Madhya Pradesh")) {
					mpCount = count.intValue();
				} else if (state.equalsIgnoreCase("Bihar")) {
					biharCount = count.intValue();
				} else {
					othersCount += count.intValue();
				}
			}
		}

		if (upCount > 0) {
			Map<String, Object> upMap = new LinkedHashMap<>();
			upMap.put("name", "UP");
			upMap.put("value", upCount);
			regionDistribution.add(upMap);
		}

		if (rajasthanCount > 0) {
			Map<String, Object> rajasthanMap = new LinkedHashMap<>();
			rajasthanMap.put("name", "Rajasthan");
			rajasthanMap.put("value", rajasthanCount);
			regionDistribution.add(rajasthanMap);
		}

		if (mpCount > 0) {
			Map<String, Object> mpMap = new LinkedHashMap<>();
			mpMap.put("name", "MP");
			mpMap.put("value", mpCount);
			regionDistribution.add(mpMap);
		}

		if (biharCount > 0) {
			Map<String, Object> biharMap = new LinkedHashMap<>();
			biharMap.put("name", "Bihar");
			biharMap.put("value", biharCount);
			regionDistribution.add(biharMap);
		}

		Map<String, Object> othersMap = new LinkedHashMap<>();
		othersMap.put("name", "Others");
		othersMap.put("value", othersCount);
		regionDistribution.add(othersMap);

		return regionDistribution;
	}

	private List<Map<String, Object>> getPaymentTrend(LocalDateTime fromDate, LocalDateTime toDate) {
		List<Map<String, Object>> paymentTrend = new ArrayList<>();

		// Get last 3 months of payment data
		LocalDate now = LocalDate.now();

		// Create list of last 3 months
		for (int i = 2; i >= 0; i--) {
			LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
			LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

			LocalDateTime startDateTime = monthStart.atStartOfDay();
			LocalDateTime endDateTime = monthEnd.atTime(23, 59, 59);

			// Apply custom date range if provided
			if (fromDate != null && startDateTime.isBefore(fromDate)) {
				startDateTime = fromDate;
			}
			if (toDate != null && endDateTime.isAfter(toDate)) {
				endDateTime = toDate;
			}

			BigDecimal totalAmount = null;
			if (!startDateTime.isAfter(endDateTime)) {
				totalAmount = razorpayPaymentRepository.sumSuccessfulPaymentsByDateRange(PaymentStatus.CAPTURED,
						startDateTime, endDateTime);
			}

			String monthName = monthStart.format(DateTimeFormatter.ofPattern("MMM"));

			Map<String, Object> monthMap = new LinkedHashMap<>();
			monthMap.put("month", monthName);
			monthMap.put("amount", totalAmount != null ? totalAmount.longValue() : 0L);
			paymentTrend.add(monthMap);
		}

		return paymentTrend;
	}

	private List<Profile> getProfilesByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
		if (fromDate != null && toDate != null) {
			Page<Profile> profilePage = profileRepository.findByCreatedAtBetween(fromDate, toDate,
					PageRequest.of(0, Integer.MAX_VALUE));
			return profilePage.getContent();
		} else if (fromDate != null) {
			Page<Profile> profilePage = profileRepository.findByCreatedAtGreaterThanEqual(fromDate,
					PageRequest.of(0, Integer.MAX_VALUE));
			return profilePage.getContent();
		} else if (toDate != null) {
			Page<Profile> profilePage = profileRepository.findByCreatedAtLessThanEqual(toDate,
					PageRequest.of(0, Integer.MAX_VALUE));
			return profilePage.getContent();
		}
		return profileRepository.findAll();
	}

	public Map<String, Object> getAdvanceAnalysisData(LocalDateTime fromDate, LocalDateTime toDate) {
		Map<String, Object> payload = new LinkedHashMap<>();

		payload.put("totalProfiles", getTotalProfilesCount(fromDate, toDate));

		payload.put("genderRatio", getGenderRatio(fromDate, toDate));

		payload.put("userGrowth", getUserGrowth(fromDate, toDate));

		payload.put("ageDistributionByGender", getAgeDistributionByGender(fromDate, toDate));

		payload.put("regionDistribution", getRegionDistributionWithMoreStates(fromDate, toDate));

		return payload;
	}

	private Map<String, Integer> getGenderRatio(LocalDateTime fromDate, LocalDateTime toDate) {
		Map<String, Integer> genderRatio = new LinkedHashMap<>();

		Long maleCount;
		Long femaleCount;

		if (fromDate != null || toDate != null) {
			maleCount = profileRepository.countByGenderAndDateRange(Gender.MALE, fromDate, toDate);
			femaleCount = profileRepository.countByGenderAndDateRange(Gender.FEMALE, fromDate, toDate);
		} else {
			maleCount = profileRepository.countByGender(Gender.MALE);
			femaleCount = profileRepository.countByGender(Gender.FEMALE);
		}

		genderRatio.put("male", maleCount != null ? maleCount.intValue() : 0);
		genderRatio.put("female", femaleCount != null ? femaleCount.intValue() : 0);

		return genderRatio;
	}

	private List<Map<String, Object>> getUserGrowth(LocalDateTime fromDate, LocalDateTime toDate) {
		List<Map<String, Object>> userGrowth = new ArrayList<>();

		// Get last 3 months of user registration data
		LocalDate now = LocalDate.now();

		for (int i = 2; i >= 0; i--) {
			LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
			LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

			LocalDateTime startDateTime = monthStart.atStartOfDay();
			LocalDateTime endDateTime = monthEnd.atTime(23, 59, 59);

			// Apply custom date range if provided
			if (fromDate != null && startDateTime.isBefore(fromDate)) {
				startDateTime = fromDate;
			}
			if (toDate != null && endDateTime.isAfter(toDate)) {
				endDateTime = toDate;
			}

			Long userCount = 0L;
			if (!startDateTime.isAfter(endDateTime)) {
				userCount = userRepository.countByCreatedAtBetween(startDateTime, endDateTime);
			}

			String monthName = monthStart.format(DateTimeFormatter.ofPattern("MMM"));

			Map<String, Object> monthMap = new LinkedHashMap<>();
			monthMap.put("month", monthName);
			monthMap.put("users", userCount != null ? userCount : 0L);
			userGrowth.add(monthMap);
		}

		return userGrowth;
	}

	private List<Map<String, Object>> getAgeDistributionByGender(LocalDateTime fromDate, LocalDateTime toDate) {
		List<Map<String, Object>> ageDistribution = new ArrayList<>();

		// Get all profiles within date range
		List<Profile> profiles = getProfilesByDateRange(fromDate, toDate);

		// Initialize counters for each age range and gender
		int male18_22 = 0, female18_22 = 0;
		int male23_27 = 0, female23_27 = 0;
		int male28_32 = 0, female28_32 = 0;
		int male33_37 = 0, female33_37 = 0;
		int male38Plus = 0, female38Plus = 0;

		LocalDate now = LocalDate.now();

		for (Profile profile : profiles) {
			LocalDate dob = profile.getDateOfBirth();
			if (dob != null) {
				int age = Period.between(dob, now).getYears();
				Gender gender = profile.getGender();

				if (age >= 18 && age <= 22) {
					if (gender == Gender.MALE) {
						male18_22++;
					} else if (gender == Gender.FEMALE) {
						female18_22++;
					}
				} else if (age >= 23 && age <= 27) {
					if (gender == Gender.MALE) {
						male23_27++;
					} else if (gender == Gender.FEMALE) {
						female23_27++;
					}
				} else if (age >= 28 && age <= 32) {
					if (gender == Gender.MALE) {
						male28_32++;
					} else if (gender == Gender.FEMALE) {
						female28_32++;
					}
				} else if (age >= 33 && age <= 37) {
					if (gender == Gender.MALE) {
						male33_37++;
					} else if (gender == Gender.FEMALE) {
						female33_37++;
					}
				} else if (age >= 38) {
					if (gender == Gender.MALE) {
						male38Plus++;
					} else if (gender == Gender.FEMALE) {
						female38Plus++;
					}
				}
			}
		}

		ageDistribution.add(createAgeGenderMap("18-22", male18_22, female18_22));
		ageDistribution.add(createAgeGenderMap("23-27", male23_27, female23_27));
		ageDistribution.add(createAgeGenderMap("28-32", male28_32, female28_32));
		ageDistribution.add(createAgeGenderMap("33-37", male33_37, female33_37));
		ageDistribution.add(createAgeGenderMap("38+", male38Plus, female38Plus));

		return ageDistribution;
	}

	private Map<String, Object> createAgeGenderMap(String range, int male, int female) {
		Map<String, Object> ageMap = new LinkedHashMap<>();
		ageMap.put("range", range);
		ageMap.put("male", male);
		ageMap.put("female", female);
		return ageMap;
	}

	private List<Map<String, Object>> getRegionDistributionWithMoreStates(LocalDateTime fromDate,
			LocalDateTime toDate) {
		List<Map<String, Object>> regionDistribution = new ArrayList<>();

		List<Object[]> results;
		if (fromDate != null && toDate != null) {
			results = locationRepository.countByStateAndProfileCreatedAtBetween(fromDate, toDate);
		} else if (fromDate != null) {
			results = locationRepository.countByStateAndProfileCreatedAtAfter(fromDate);
		} else if (toDate != null) {
			results = locationRepository.countByStateAndProfileCreatedAtBefore(toDate);
		} else {
			results = locationRepository.countByStateGrouped();
		}

		List<String> priorityStates = Arrays.asList("UP", "Rajasthan", "MP", "Bihar", "Maharashtra", "Delhi NCR");
		Map<String, Integer> stateCounts = new LinkedHashMap<>();
		int othersCount = 0;

		for (Object[] result : results) {
			String state = (String) result[0];
			Long count = (Long) result[1];

			if (state != null) {
				if (priorityStates.contains(state)) {
					stateCounts.put(state, count.intValue());
				} else if (state.equalsIgnoreCase("UP") || state.equalsIgnoreCase("Uttar Pradesh")) {
					stateCounts.put("UP", count.intValue());
				} else if (state.equalsIgnoreCase("Rajasthan")) {
					stateCounts.put("Rajasthan", count.intValue());
				} else if (state.equalsIgnoreCase("MP") || state.equalsIgnoreCase("Madhya Pradesh")) {
					stateCounts.put("MP", count.intValue());
				} else if (state.equalsIgnoreCase("Bihar")) {
					stateCounts.put("Bihar", count.intValue());
				} else {
					othersCount += count.intValue();
				}
			}

		}

		for (String state : priorityStates) {
			if (stateCounts.containsKey(state)) {
				Map<String, Object> regionMap = new LinkedHashMap<>();
				regionMap.put("name", state);
				regionMap.put("value", stateCounts.get(state));
				regionDistribution.add(regionMap);
			}
		}

		Map<String, Object> othersMap = new LinkedHashMap<>();
		othersMap.put("name", "Others");
		othersMap.put("value", othersCount);
		regionDistribution.add(othersMap);

		return regionDistribution;
	}

}