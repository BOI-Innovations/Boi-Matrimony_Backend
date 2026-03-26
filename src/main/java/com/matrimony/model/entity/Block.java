package com.matrimony.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "blocks", uniqueConstraints = @UniqueConstraint(columnNames = { "blocker_id", "blocked_id" }))
public class Block {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blocker_id", nullable = false)
	private User blocker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blocked_id", nullable = false)
	private User blocked;

	@Column(length = 500)
	private String reason;

	@Column(nullable = false)
	private LocalDateTime blockedAt;

	@PrePersist
	protected void onCreate() {
		blockedAt = LocalDateTime.now();
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getBlocker() {
		return blocker;
	}

	public void setBlocker(User blocker) {
		this.blocker = blocker;
	}

	public User getBlocked() {
		return blocked;
	}

	public void setBlocked(User blocked) {
		this.blocked = blocked;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getBlockedAt() {
		return blockedAt;
	}

	public void setBlockedAt(LocalDateTime blockedAt) {
		this.blockedAt = blockedAt;
	}
}