package com.matrimony.listener;

import com.matrimony.event.UserRegistrationEvent;
import com.matrimony.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationListener {

    @Autowired
    private EmailService emailService;

    @Async
    @EventListener
    public void handleUserRegistrationEvent(UserRegistrationEvent event) {
        // Send welcome email
        emailService.sendWelcomeEmail(
            event.getUser().getEmail(),
            event.getUser().getUsername()
        );

        // Send verification email
        emailService.sendVerificationEmail(
            event.getUser().getEmail(),
            event.getUser().getVerificationToken()
        );
    }
}