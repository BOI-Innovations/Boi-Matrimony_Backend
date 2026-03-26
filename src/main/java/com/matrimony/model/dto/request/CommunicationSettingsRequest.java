package com.matrimony.model.dto.request;

public class CommunicationSettingsRequest {

	private Boolean emailNotifications;
	private Boolean smsNotifications;
	private Boolean matchAlerts;
	private Boolean messageNotifications;
	private Boolean profileViewAlerts;
	private Boolean weeklyDigest;
	private Boolean promotionalEmails;

	public Boolean getEmailNotifications() {
		return emailNotifications;
	}

	public void setEmailNotifications(Boolean emailNotifications) {
		this.emailNotifications = emailNotifications;
	}

	public Boolean getSmsNotifications() {
		return smsNotifications;
	}

	public void setSmsNotifications(Boolean smsNotifications) {
		this.smsNotifications = smsNotifications;
	}

	public Boolean getMatchAlerts() {
		return matchAlerts;
	}

	public void setMatchAlerts(Boolean matchAlerts) {
		this.matchAlerts = matchAlerts;
	}

	public Boolean getMessageNotifications() {
		return messageNotifications;
	}

	public void setMessageNotifications(Boolean messageNotifications) {
		this.messageNotifications = messageNotifications;
	}

	public Boolean getProfileViewAlerts() {
		return profileViewAlerts;
	}

	public void setProfileViewAlerts(Boolean profileViewAlerts) {
		this.profileViewAlerts = profileViewAlerts;
	}

	public Boolean getWeeklyDigest() {
		return weeklyDigest;
	}

	public void setWeeklyDigest(Boolean weeklyDigest) {
		this.weeklyDigest = weeklyDigest;
	}

	public Boolean getPromotionalEmails() {
		return promotionalEmails;
	}

	public void setPromotionalEmails(Boolean promotionalEmails) {
		this.promotionalEmails = promotionalEmails;
	}
}
