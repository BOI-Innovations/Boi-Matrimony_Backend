package com.matrimony.event;

import org.springframework.context.ApplicationEvent;

import com.matrimony.model.entity.User;

public class UserRegistrationEvent extends ApplicationEvent {
	private final User user;

	public UserRegistrationEvent(Object source, User user) {
		super(source);
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}