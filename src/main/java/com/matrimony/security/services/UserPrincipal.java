package com.matrimony.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrimony.model.entity.User;

public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;
	private String email;
	private String phoneNumber;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(
			Long id,
			String username,
			String email,
			String phoneNumber,
			String password,
			Collection<? extends GrantedAuthority> authorities) {

		this.id = id;
		this.username = username;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserPrincipal create(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toList());

		return new UserPrincipal(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getPhoneNumber(),
				user.getPassword(),
				authorities
		);
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	// ===== Role helpers =====

	public boolean hasRole(String roleName) {
		return authorities.stream()
				.anyMatch(authority -> authority.getAuthority().equals(roleName));
	}

	public boolean hasAnyRole(String... roleNames) {
		for (String roleName : roleNames) {
			if (hasRole(roleName)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAdmin() {
		return hasRole("ROLE_ADMIN");
	}

	public boolean isModerator() {
		return hasRole("ROLE_MODERATOR");
	}

	public boolean isUser() {
		return hasRole("ROLE_USER");
	}

	public String getRole() {
		return authorities.stream()
				.findFirst()
				.map(GrantedAuthority::getAuthority)
				.orElse("ROLE_USER");
	}
}
