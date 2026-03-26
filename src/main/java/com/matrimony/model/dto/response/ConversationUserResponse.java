package com.matrimony.model.dto.response;

public class ConversationUserResponse {

	private Long id;
	private String username;
	private String email;
	private String occupation;
	private Long profileId;
	private String firstName;
	private String lastName;
	private Long unreadCount;

	public ConversationUserResponse(Long id, String username, String email, String occupation, Long profileId,
			String firstName, String lastName, Long unreadCount) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.occupation = occupation;
		this.profileId = profileId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.unreadCount = unreadCount;
	}

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

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(Long unreadCount) {
		this.unreadCount = unreadCount;
	}

}
