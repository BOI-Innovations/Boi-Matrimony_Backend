package com.matrimony.scheduler;

import com.matrimony.model.entity.UserSubscription;
import com.matrimony.model.enums.SubscriptionStatus;
import com.matrimony.repository.UserSubscriptionRepository;
import com.matrimony.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SubscriptionExpiryScheduler {

    @Autowired
    private UserSubscriptionRepository subscriptionRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Warn users 3 days before subscription expiry
     * Runs daily at 9 AM
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void checkExpiringSubscriptions() {
        LocalDate today = LocalDate.now();
        LocalDate warningDate = today.plusDays(3);

        List<UserSubscription> expiringSubscriptions = subscriptionRepository
                .findByStatus(SubscriptionStatus.ACTIVE)
                .stream()
                .filter(sub ->
                        sub.getEndDate().isAfter(today) &&
                        !sub.getEndDate().isAfter(warningDate)
                )
                .toList();

        for (UserSubscription subscription : expiringSubscriptions) {
            emailService.sendSubscriptionExpiryEmail(
                    subscription.getUser().getEmail(),
                    subscription.getUser().getUsername(),
                    subscription.getPlan().getName()
            );
        }
    }

    /**
     * Expire subscriptions after end date
     * Runs daily at midnight
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void expireSubscriptions() {
        LocalDate today = LocalDate.now();

        List<UserSubscription> expiredSubscriptions = subscriptionRepository
                .findByStatus(SubscriptionStatus.ACTIVE)
                .stream()
                .filter(sub -> sub.getEndDate().isBefore(today))
                .toList();

        for (UserSubscription subscription : expiredSubscriptions) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
        }

        subscriptionRepository.saveAll(expiredSubscriptions);
    }
}
