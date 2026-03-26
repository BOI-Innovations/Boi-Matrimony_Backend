package com.matrimony.serviceImpl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.matrimony.model.entity.User;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	ProfileRepository profileRepository;

	@Async
	@Override
	public void sendWelcomeEmail(String to, String username) {
		String subject = "Welcome to Matrimony Application";
		String content = "<p>Dear " + username + ",</p>"
				+ "<p>Welcome to the Matrimony platform! We're excited to have you on board.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(to, subject, content);
	}

	@Async
	@Override
	public void sendWelcomeEmail(User user) {
		sendWelcomeEmail(user.getEmail(), user.getUsername());
	}

	@Async
	@Override
	public void sendInterestReceivedEmail(String to, String fromUsername) {
		String subject = "New Interest Received from " + fromUsername;
		String content = "<p>Hi there,</p>" + "<p>You have received a new interest from <strong>" + fromUsername
				+ "</strong>.</p>" + "<p>Login now to view the profile and take action.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(to, subject, content);
	}

	@Async
	@Override
	public void sendInterestReceivedEmail(User receiver, User sender, String message) {
		String subject = "New Interest Received from " + sender.getUsername();
		String content = "<p>Hi " + receiver.getUsername() + ",</p>"
				+ "<p>You have received a new interest from <strong>" + sender.getUsername() + "</strong>.</p>"
				+ "<p>Message: " + message + "</p>" + "<p>Login now to respond.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(receiver.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendSubscriptionExpiryEmail(String to, String username, String planName) {
		String subject = "Your " + planName + " Subscription is Expiring Soon";
		String content = "<p>Dear " + username + ",</p>" + "<p>Your <strong>" + planName
				+ "</strong> subscription is about to expire.</p>"
				+ "<p>Renew now to continue enjoying our premium features.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(to, subject, content);
	}

	@Async
	@Override
	public void sendSubscriptionExpiredEmail(User user, String planName) {
		String subject = "Your " + planName + " Subscription Has Expired";
		String content = "<p>Dear " + user.getUsername() + ",</p>" + "<p>Your <strong>" + planName
				+ "</strong> subscription has expired.</p>"
				+ "<p>Please renew your subscription to regain access to premium features.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(user.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendVerificationEmail(String to, String token) {
		String subject = "Verify Your Email Address";
		String verificationLink = "https://yourdomain.com/verify?token=" + token;
		String content = "<p>Please click the link below to verify your email address:</p>" + "<p><a href=\""
				+ verificationLink + "\">Verify Email</a></p>"
				+ "<p>If you did not register, please ignore this email.</p>";
		sendHtmlEmail(to, subject, content);
	}

	@Async
	@Override
	public void sendVerificationEmail(User user, String verificationToken) {
		String subject = "Verify Your Email Address";
		String verificationLink = "https://yourdomain.com/verify?token=" + verificationToken;
		String content = "<p>Dear " + user.getUsername() + ",</p>"
				+ "<p>Please click the link below to verify your email address:</p>" + "<p><a href=\""
				+ verificationLink + "\">Verify Email</a></p>"
				+ "<p>If you did not register, please ignore this email.</p>";
		sendHtmlEmail(user.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendEmailVerificationSuccess(User user) {
		String subject = "Email Verified Successfully";
		String content = "<p>Dear " + user.getUsername() + ",</p>"
				+ "<p>Your email address has been successfully verified.</p>"
				+ "<p>Thank you for confirming your email.</p>" + "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(user.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendPasswordResetEmail(String to, String token) {
		String subject = "Password Reset Request";
		String resetLink = "https://yourdomain.com/reset-password?token=" + token;
		String content = "<p>You requested a password reset. Click the link below to reset your password:</p>"
				+ "<p><a href=\"" + resetLink + "\">Reset Password</a></p>"
				+ "<p>If you didn't request this, please ignore this email.</p>";
		sendHtmlEmail(to, subject, content);
	}

	@Async
	@Override
	public void sendPasswordResetEmail(User user, String resetToken) {
		String subject = "Password Reset Request";
		String resetLink = "https://yourdomain.com/reset-password?token=" + resetToken;
		String content = "<p>Dear " + user.getUsername() + ",</p>"
				+ "<p>You requested a password reset. Click the link below to reset your password:</p>"
				+ "<p><a href=\"" + resetLink + "\">Reset Password</a></p>"
				+ "<p>If you didn't request this, please ignore this email.</p>";
		sendHtmlEmail(user.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendPasswordChangedEmail(User user) {
		String subject = "Password Changed Successfully";
		String content = "<p>Dear " + user.getUsername() + ",</p>"
				+ "<p>Your password has been changed successfully. If you did not make this change, please contact support immediately.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(user.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendInterestAcceptedEmail(User sender, User receiver) {
		String subject = "Interest Accepted by " + receiver.getUsername();
		String content = "<p>Dear " + sender.getUsername() + ",</p>" + "<p>Your interest has been accepted by <strong>"
				+ receiver.getUsername() + "</strong>.</p>" + "<p>Login now to communicate further.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(sender.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendInterestRejectedEmail(User sender, User receiver) {
		String subject = "Interest Rejected by " + receiver.getUsername();
		String content = "<p>Dear " + sender.getUsername() + ",</p>" + "<p>Your interest has been rejected by <strong>"
				+ receiver.getUsername() + "</strong>.</p>" + "<p>Don't be discouraged, keep looking!</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(sender.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendMatchNotificationEmail(User user1, User user2) {
		String subject = "It's a Match!";
		String content = "<p>Dear " + user1.getUsername() + " and " + user2.getUsername() + ",</p>"
				+ "<p>You have both shown interest in each other. Start chatting now!</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(user1.getEmail(), subject, content);
		sendHtmlEmail(user2.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendNewMessageEmail(User receiver, User sender, String messagePreview) {
		String subject = "New Message from " + sender.getUsername();
		String content = "<p>Dear " + receiver.getUsername() + ",</p>"
				+ "<p>You have received a new message from <strong>" + sender.getUsername() + "</strong>:</p>"
				+ "<blockquote>" + messagePreview + "</blockquote>" + "<p>Login now to reply.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(receiver.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendMessageReadNotification(User sender, User receiver) {
		String subject = "Your Message Was Read by " + receiver.getUsername();
		String content = "<p>Dear " + sender.getUsername() + ",</p>" + "<p>Your message to <strong>"
				+ receiver.getUsername() + "</strong> has been read.</p>" + "<p>Keep the conversation going!</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(sender.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendSubscriptionConfirmation(Long userId, String email, String planName) {
		String fullName = profileRepository.findFullNameByUserId(userId);

		String subject = "Subscription Confirmed: " + planName;

		String content = "<p>Dear " + fullName + ",</p>" + "<p>Your subscription to the <strong>" + planName
				+ "</strong> plan is now active.</p>" + "<p>Enjoy our premium services!</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";

		sendHtmlEmail(email, subject, content);
	}

	@Async
	@Override
	public void sendSubscriptionRenewalReminder(User user, String planName, int daysUntilExpiry) {
		String subject = "Subscription Renewal Reminder";
		String content = "<p>Dear " + user.getUsername() + ",</p>" + "<p>Your <strong>" + planName
				+ "</strong> subscription expires in " + daysUntilExpiry + " days.</p>"
				+ "<p>Please renew to avoid interruption.</p>" + "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(user.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendSubscriptionUpgradeEmail(User user, String oldPlan, String newPlan) {
		String subject = "Subscription Upgraded Successfully";
		String content = "<p>Dear " + user.getUsername() + ",</p>"
				+ "<p>Your subscription has been upgraded from <strong>" + oldPlan + "</strong> to <strong>" + newPlan
				+ "</strong>.</p>" + "<p>Enjoy the new benefits!</p>" + "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(user.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendProfileVerificationEmail(User user, boolean approved, String comments) {
		String subject = "Profile Verification " + (approved ? "Approved" : "Rejected");
		String content = "<p>Dear " + user.getUsername() + ",</p>" + "<p>Your profile verification status: <strong>"
				+ (approved ? "Approved" : "Rejected") + "</strong>.</p>"
				+ (comments != null ? "<p>Comments: " + comments + "</p>" : "") + "<p>Thank you for being with us.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(user.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendProfileCompletionReminder(User user, int completionPercentage) {
		String subject = "Complete Your Profile";
		String content = "<p>Dear " + user.getUsername() + ",</p>" + "<p>Your profile is " + completionPercentage
				+ "% complete.</p>" + "<p>Complete your profile to get better matches!</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(user.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendProfileViewedNotification(User profileOwner, User viewer) {
		String subject = "Your Profile Was Viewed";
		String content = "<p>Dear " + profileOwner.getUsername() + ",</p>" + "<p>Your profile was viewed by <strong>"
				+ viewer.getUsername() + "</strong>.</p>" + "<p>Login to see who viewed your profile.</p>"
				+ "<p>Best regards,<br/>Matrimony Team</p>";
		sendHtmlEmail(profileOwner.getEmail(), subject, content);
	}

	@Async
	@Override
	public void sendAdminAlert(String subject, String message) {
		String adminEmail = "admin@yourdomain.com"; // Set admin email here
		String content = "<p>Admin Alert:</p><p>" + message + "</p>";
		sendHtmlEmail(adminEmail, subject, content);
	}

	@Async
	@Override
	public void sendSystemNotification(String subject, String message) {
		// For system notifications, sending to admin email as example
		String adminEmail = "admin@yourdomain.com";
		String content = "<p>System Notification:</p><p>" + message + "</p>";
		sendHtmlEmail(adminEmail, subject, content);
	}

	@Async
	@Override
	public void sendBatchNotification(List<User> users, String subject, String message) {
		for (User user : users) {
			String content = "<p>Dear " + user.getUsername() + ",</p><p>" + message + "</p>"
					+ "<p>Best regards,<br/>Matrimony Team</p>";
			sendHtmlEmail(user.getEmail(), subject, content);
		}
	}

	@Async
	@Override
	public void sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> variables) {
		// Example: simple template processing (replace {{key}} with value)
		String template = getEmailTemplate(templateName);
		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			template = template.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
		}
		sendHtmlEmail(to, subject, template);
	}

	@Async
	@Override
	public void sendBulkTemplatedEmail(List<String> recipients, String subject, String templateName,
			Map<String, Object> variables) {
		String template = getEmailTemplate(templateName);
		for (String to : recipients) {
			String personalized = template;
			for (Map.Entry<String, Object> entry : variables.entrySet()) {
				personalized = personalized.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
			}
			sendHtmlEmail(to, subject, personalized);
		}
	}

	@Override
	public boolean isValidEmail(String email) {
		return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
	}

	@Override
	public boolean isEmailDeliverable(String email) {
		// Stub for real deliverability check
		return true;
	}

	@Override
	public void verifyEmailAddress(String email) {
		// Stub: Possibly send verification email or mark verified
		// For now, no-op
	}

	@Override
	public long getSentEmailsCount() {
		// Stub: Integrate with your email tracking system
		return 0;
	}

	@Override
	public long getFailedEmailsCount() {
		// Stub: Integrate with your email tracking system
		return 0;
	}

	@Override
	public Map<String, Long> getEmailStatisticsByType() {
		// Stub: Return empty map or stats if available
		return Map.of();
	}

	@Override
	public void testEmailConfiguration() {
		// Send a test email to admin or log test status
		sendAdminAlert("Test Email Configuration", "This is a test email from the email service.");
	}

	@Override
	public boolean isEmailServiceEnabled() {
		// Stub: Return true if email service enabled
		return true;
	}

	@Override
	public void setEmailServiceStatus(boolean enabled) {
		// Stub: Enable/disable email sending service
	}

	@Override
	public String getEmailTemplate(String templateName) {
		// Stub: Return simple template string based on name
		// In reality, load from DB or files
		switch (templateName) {
		case "welcome":
			return "<p>Dear {{username}},</p><p>Welcome to our Matrimony platform!</p>";
		case "passwordReset":
			return "<p>Click here to reset your password: {{resetLink}}</p>";
		default:
			return "<p>This is a default email template.</p>";
		}
	}

	@Override
	public void updateEmailTemplate(String templateName, String content) {
		// Stub: Save template content to DB or files
	}

	@Override
	public List<String> getAvailableTemplates() {
		// Stub: Return list of template names
		return List.of("welcome", "passwordReset", "interestReceived");
	}

	@Async
	@Override
	public void sendHtmlEmail(String to, String subject, String htmlContent) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);
			try {
				helper.setFrom(senderEmail, "Brahman Matrimony");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send email to " + to, e);
		}
	}
}
