package com.matrimony.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "communication_settings")
public class CommunicationSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	@JsonIgnore
	private User user;
	private Boolean emailNotifications;
	private Boolean smsNotifications;
	private Boolean matchAlerts;
	private Boolean messageNotifications;
	private Boolean profileViewAlerts;
	private Boolean weeklyDigest;
	private Boolean promotionalEmails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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
