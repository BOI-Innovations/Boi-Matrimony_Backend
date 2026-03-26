package com.matrimony.listener;

import com.matrimony.event.InterestEvent;
import com.matrimony.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class InterestEventListener {

    @Autowired
    private EmailService emailService;

    @Async
    @EventListener
    public void handleInterestEvent(InterestEvent event) {
        if ("SENT".equals(event.getEventType())) {
            // Send notification to the recipient
            emailService.sendInterestReceivedEmail(
                event.getInterest().getToUser().getEmail(),
                event.getInterest().getFromUser().getUsername()
            );
        }
    }
}