package com.matrimony.scheduler;

import com.matrimony.model.entity.Interest;
import com.matrimony.repository.InterestRepository;
import com.matrimony.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 10 * * ?") // Run daily at 10 AM
    public void sendDailyMatchSuggestions() {
        // This would typically involve:
        // 1. Finding users who want daily notifications
        // 2. Generating match suggestions for them
        // 3. Sending email notifications
        
        // Implementation would depend on user preference settings
    }

    @Scheduled(cron = "0 0 18 * * ?") // Run daily at 6 PM
    public void sendInterestReminders() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        
        // Find interests that haven't been responded to for 24 hours
        List<Interest> pendingInterests = interestRepository.findAll().stream()
                .filter(interest -> interest.getStatus().name().equals("PENDING") &&
                                   interest.getSentAt().isBefore(yesterday))
                .toList();

        for (Interest interest : pendingInterests) {
            // Send reminder to the recipient
            emailService.sendInterestReceivedEmail(
                interest.getToUser().getEmail(),
                interest.getFromUser().getUsername()
            );
        }
    }
}