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

        // 👤 Users
        if (userFromDate != null || userToDate != null) {
            payload.put("totalUsers", userRepository.countByCreatedAtBetween(userFromDate, userToDate));
            payload.put("activeUsers", userRepository.countActiveUsersByDateRange(userFromDate, userToDate));
            payload.put("inactiveUsers", userRepository.countInactiveUsersByDateRange(userFromDate, userToDate));
            payload.put("newToday", userRepository.countByCreatedAtBetween(todayStart, todayEnd));
            
            payload.put("verifiedProfiles", 
                profileRepository.countVerifiedProfilesByDateRange(
                    ProfileVerificationStatus.VERIFIED, 
                    userFromDate, 
                    userToDate
                )
            );
            
            payload.put("activeProfiles", 
                profileRepository.countActiveProfilesByDateRange(
                    userFromDate, 
                    userToDate
                )
            );
            
            // ✅ CORRECTED: Added status parameter
            payload.put("suspendedProfiles", 
                profileRepository.countSuspendedProfilesByDateRange(
                    ProfileVerificationStatus.SUSPENDED, 
                    userFromDate, 
                    userToDate
                )
            );
            
            payload.put("genderSplit", getGenderSplitByDateRange(userFromDate, userToDate));
            payload.put("monthlyRegistrations", getMonthlyRegistrations(userFromDate, userToDate));
        } else {
            payload.put("totalUsers", userRepository.count());
            payload.put("activeUsers", userRepository.countByIsActiveTrue());
            payload.put("inactiveUsers", userRepository.countByIsActiveFalse());
            payload.put("newToday", userRepository.countByCreatedAtBetween(todayStart, todayEnd));
            payload.put("verifiedProfiles", profileRepository.countByVerificationStatus(ProfileVerificationStatus.VERIFIED));
            payload.put("activeProfiles", profileRepository.countActiveProfiles());
            payload.put("suspendedProfiles", profileRepository.countByVerificationStatus(ProfileVerificationStatus.SUSPENDED));
            payload.put("genderSplit", getGenderSplit());
            payload.put("monthlyRegistrations", getMonthlyRegistrations(null, null));
        }

        // Premium & Subscriptions
        payload.put("premiumMembers", userSubscriptionRepository.countDistinctUsersWithActiveSubscription(LocalDate.now()));
        payload.put("activeSubscriptions", userSubscriptionRepository.countByStatusAndEndDateAfter(SubscriptionStatus.ACTIVE, LocalDate.now()));

        // 💰 Payments
        if (paymentFromDate != null || paymentToDate != null) {
            BigDecimal totalRevenueBD = razorpayPaymentRepository.sumSuccessfulPaymentsByDateRange(
                PaymentStatus.CAPTURED, paymentFromDate, paymentToDate
            );
            payload.put("totalRevenue", totalRevenueBD != null ? totalRevenueBD.doubleValue() : 0.0);
        } else {
            BigDecimal totalRevenueBD = razorpayPaymentRepository.sumSuccessfulPayments(PaymentStatus.CAPTURED);
            payload.put("totalRevenue", totalRevenueBD != null ? totalRevenueBD.doubleValue() : 0.0);
        }

        // 📋 Complaints
        if (userFromDate != null || userToDate != null) {
            Long totalHelpRequests = helpRequestRepository.countByCreatedAtBetween(userFromDate, userToDate);
            Long totalReports = reportRepository.countByCreatedAtBetween(userFromDate, userToDate);
            payload.put("totalComplaints", totalHelpRequests + totalReports);
            
            Long resolvedComplaints = helpRequestRepository.countByStatusAndCreatedAtBetween("RESOLVED", userFromDate, userToDate) +  
                                      reportRepository.countByStatusAndCreatedAtBetween(ReportStatus.RESOLVED, userFromDate, userToDate);
            payload.put("resolvedComplaints", resolvedComplaints);
            
            Long pendingComplaints = helpRequestRepository.countByStatusAndCreatedAtBetween("OPEN", userFromDate, userToDate) +  
                                     reportRepository.countByStatusAndCreatedAtBetween(ReportStatus.PENDING, userFromDate, userToDate);
            payload.put("pendingComplaints", pendingComplaints);
        } else {
            Long totalHelpRequests = helpRequestRepository.count();
            Long totalReports = reportRepository.count();
            payload.put("totalComplaints", totalHelpRequests + totalReports);
            
            Long resolvedComplaints = helpRequestRepository.countByStatus("RESOLVED") +  
                                      reportRepository.countByStatus(ReportStatus.RESOLVED);
            payload.put("resolvedComplaints", resolvedComplaints);
            
            Long pendingComplaints = helpRequestRepository.countByStatus("OPEN") +  
                                     reportRepository.countByStatus(ReportStatus.PENDING);
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
}