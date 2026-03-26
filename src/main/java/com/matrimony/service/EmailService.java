package com.matrimony.service;

import java.util.List;
import java.util.Map;

import com.matrimony.model.entity.User;

public interface EmailService {
	void sendWelcomeEmail(String to, String username);

	void sendInterestReceivedEmail(String to, String fromUsername);

	void sendSubscriptionExpiryEmail(String to, String username, String planName);

	void sendVerificationEmail(String to, String token);

	void sendPasswordResetEmail(String to, String token);

	void sendWelcomeEmail(User user);

	void sendVerificationEmail(User user, String verificationToken);

	void sendEmailVerificationSuccess(User user);

	void sendPasswordResetEmail(User user, String resetToken);

	void sendPasswordChangedEmail(User user);

	void sendInterestReceivedEmail(User receiver, User sender, String message);

	void sendInterestAcceptedEmail(User sender, User receiver);

	void sendInterestRejectedEmail(User sender, User receiver);

	void sendMatchNotificationEmail(User user1, User user2);

	void sendNewMessageEmail(User receiver, User sender, String messagePreview);

	void sendMessageReadNotification(User sender, User receiver);

	void sendSubscriptionRenewalReminder(User user, String planName, int daysUntilExpiry);

	void sendSubscriptionExpiredEmail(User user, String planName);

	void sendSubscriptionUpgradeEmail(User user, String oldPlan, String newPlan);

	void sendProfileVerificationEmail(User user, boolean approved, String comments);

	void sendProfileCompletionReminder(User user, int completionPercentage);

	void sendProfileViewedNotification(User profileOwner, User viewer);

	void sendAdminAlert(String subject, String message);

	void sendSystemNotification(String subject, String message);

	void sendBatchNotification(List<User> users, String subject, String message);

	void sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> variables);

	void sendBulkTemplatedEmail(List<String> recipients, String subject, String templateName,
			Map<String, Object> variables);

	boolean isValidEmail(String email);

	boolean isEmailDeliverable(String email);

	void verifyEmailAddress(String email);

	long getSentEmailsCount();

	long getFailedEmailsCount();

	Map<String, Long> getEmailStatisticsByType();

	void testEmailConfiguration();

	boolean isEmailServiceEnabled();

	void setEmailServiceStatus(boolean enabled);

	String getEmailTemplate(String templateName);

	void updateEmailTemplate(String templateName, String content);

	List<String> getAvailableTemplates();

	void sendHtmlEmail(String to, String subject, String htmlContent);

	void sendSubscriptionConfirmation(Long userId, String email, String planName);
}
