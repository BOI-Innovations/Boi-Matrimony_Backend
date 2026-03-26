package com.matrimony.model.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscription_plan")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SubscriptionPlan {

	@Id
	@Column(name = "plan_id", nullable = false, unique = true, updatable = false)
	private String planId;

	@Column(nullable = false, unique = true)
	private String code; // BASIC, PREMIUM, GOLD, PLATINUM

	@Column(nullable = false)
	private String name; // Basic Plan, Premium Plan

	@Column(nullable = false)
	private Long price; // in rupees

	@Column(nullable = false)
	private Integer durationDays;

	@Column(nullable = false)
	private Boolean isActive = true;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private String currency;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		int months = durationDays / 30;
		this.planId = "PLAN_" + code + "_" + months + "M";
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Integer getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(Integer durationDays) {
		this.durationDays = durationDays;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
