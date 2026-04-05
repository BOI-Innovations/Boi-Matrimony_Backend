package com.matrimony.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matrimony.model.enums.Gender;
import com.matrimony.model.enums.PaymentStatus;
import com.matrimony.model.enums.ProfileVerificationStatus;
import com.matrimony.model.enums.ReportStatus;
import com.matrimony.model.enums.SubscriptionStatus;
import com.matrimony.repository.*;

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

    public Map<String, Object> getDashboardData() {
        Map<String, Object> payload = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime todayEnd = now.withHour(23).withMinute(59).withSecond(59);

        // 👤 Users
        payload.put("totalUsers", userRepository.count());
        payload.put("activeUsers", userRepository.countByIsActiveTrue());
        payload.put("inactiveUsers", userRepository.countByIsActiveFalse());
        payload.put("newToday", userRepository.countByCreatedAtBetween(todayStart, todayEnd));

        payload.put("verifiedProfiles",
                profileRepository.countByVerificationStatus(ProfileVerificationStatus.VERIFIED));

        payload.put("activeProfiles", profileRepository.countActiveProfiles());

        payload.put("suspendedProfiles",
                profileRepository.countByVerificationStatus(ProfileVerificationStatus.SUSPENDED));

        payload.put("premiumMembers",
                userSubscriptionRepository.countDistinctUsersWithActiveSubscription(LocalDate.now()));

        payload.put("activeSubscriptions",
                userSubscriptionRepository.countByStatusAndEndDateAfter(
                        SubscriptionStatus.ACTIVE, LocalDate.now()));

        BigDecimal totalRevenueBD =
                razorpayPaymentRepository.sumSuccessfulPayments(PaymentStatus.CAPTURED);

        payload.put("totalRevenue",
                totalRevenueBD != null ? totalRevenueBD.doubleValue() : 0.0);

        Long totalHelpRequests = helpRequestRepository.count();
        Long totalReports = reportRepository.count();
        payload.put("totalComplaints", totalHelpRequests + totalReports);

        Long resolvedComplaints =
                helpRequestRepository.countByStatus("RESOLVED") +  
                reportRepository.countByStatus(ReportStatus.RESOLVED); 

        payload.put("resolvedComplaints", resolvedComplaints);

        Long pendingComplaints =
                helpRequestRepository.countByStatus("OPEN") +  
                reportRepository.countByStatus(ReportStatus.PENDING); 

        payload.put("pendingComplaints", pendingComplaints);

        Map<String, Long> genderSplit = new HashMap<>();
        genderSplit.put("male", profileRepository.countByGender(Gender.MALE));
        genderSplit.put("female", profileRepository.countByGender(Gender.FEMALE));
        payload.put("genderSplit", genderSplit);

        payload.put("monthlyRegistrations", getMonthlyRegistrations());

        return payload;
    }

    private List<Map<String, Object>> getMonthlyRegistrations() {
        List<Map<String, Object>> result = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (int i = 11; i >= 0; i--) {
            LocalDateTime monthStart = now.minusMonths(i)
                    .withDayOfMonth(1)
                    .withHour(0).withMinute(0).withSecond(0);

            LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);

            String monthName = monthStart.format(DateTimeFormatter.ofPattern("MMM"));

            Long registrations =
                    userRepository.countByCreatedAtBetween(monthStart, monthEnd);

            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", monthName);
            monthData.put("registrations", registrations);

            result.add(monthData);
        }

        return result;
    }
}