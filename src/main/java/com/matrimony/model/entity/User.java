package com.matrimony.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrimony.model.enums.RoleName;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	@NotBlank
	@Size(max = 15)
	@Column(name = "phone_number", unique = true)
	private String phoneNumber;

	@NotBlank
	@Size(max = 120)
	private String password;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Set<RoleName> roles = new HashSet<>();

	private Boolean isActive = true;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime lastLoginAt;
	private String verificationToken;
	private Boolean emailVerified = false;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private Profile profile;

	@OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Interest> sentInterests = new ArrayList<>();

	@OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Interest> receivedInterests = new ArrayList<>();

	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Message> sentMessages = new ArrayList<>();

	@OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Message> receivedMessages = new ArrayList<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UserSubscription> subscriptions = new ArrayList<>();

	@OneToMany(mappedBy = "blocker", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Block> blockedUsers = new ArrayList<>();

	@OneToMany(mappedBy = "blocked", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Block> blockedByUsers = new ArrayList<>();

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private ContactDetails contactDetails;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private CommunicationSettings communicationSettings;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private PrivacySettings privacySettings;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<HelpRequest> helpRequests = new ArrayList<>();

	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Connection> sentConnections = new ArrayList<>();

	@OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Connection> receivedConnections = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public void addSentInterest(Interest interest) {
		sentInterests.add(interest);
		interest.setFromUser(this);
	}

	public void addReceivedInterest(Interest interest) {
		receivedInterests.add(interest);
		interest.setToUser(this);
	}

	public void addSentMessage(Message message) {
		sentMessages.add(message);
		message.setSender(this);
	}

	public void addReceivedMessage(Message message) {
		receivedMessages.add(message);
		message.setReceiver(this);
	}

	public List<Connection> getSentConnections() {
		return sentConnections;
	}

	public void setSentConnections(List<Connection> sentConnections) {
		this.sentConnections = sentConnections;
	}

	public List<Connection> getReceivedConnections() {
		return receivedConnections;
	}

	public void setReceivedConnections(List<Connection> receivedConnections) {
		this.receivedConnections = receivedConnections;
	}

	public void addSubscription(UserSubscription subscription) {
		subscriptions.add(subscription);
		subscription.setUser(this);
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RoleName> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleName> roles) {
		this.roles = roles;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
		if (profile != null) {
			profile.setUser(this);
		}
	}

	public List<Interest> getSentInterests() {
		return sentInterests;
	}

	public void setSentInterests(List<Interest> sentInterests) {
		this.sentInterests = sentInterests;
	}

	public List<Interest> getReceivedInterests() {
		return receivedInterests;
	}

	public void setReceivedInterests(List<Interest> receivedInterests) {
		this.receivedInterests = receivedInterests;
	}

	public List<Message> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(List<Message> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<Message> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}

	public List<UserSubscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<UserSubscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public List<Block> getBlockedUsers() {
		return blockedUsers;
	}

	public void setBlockedUsers(List<Block> blockedUsers) {
		this.blockedUsers = blockedUsers;
	}

	public List<Block> getBlockedByUsers() {
		return blockedByUsers;
	}

	public void setBlockedByUsers(List<Block> blockedByUsers) {
		this.blockedByUsers = blockedByUsers;
	}

	public ContactDetails getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(ContactDetails contactDetails) {
		this.contactDetails = contactDetails;
	}

	public CommunicationSettings getCommunicationSettings() {
		return communicationSettings;
	}

	public void setCommunicationSettings(CommunicationSettings communicationSettings) {
		this.communicationSettings = communicationSettings;
	}

	public PrivacySettings getPrivacySettings() {
		return privacySettings;
	}

	public void setPrivacySettings(PrivacySettings privacySettings) {
		this.privacySettings = privacySettings;
	}

	public List<HelpRequest> getHelpRequests() {
		return helpRequests;
	}

	public void setHelpRequests(List<HelpRequest> helpRequests) {
		this.helpRequests = helpRequests;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}