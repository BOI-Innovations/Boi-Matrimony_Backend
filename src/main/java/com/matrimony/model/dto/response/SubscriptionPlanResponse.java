package com.matrimony.model.dto.response;

public class SubscriptionPlanResponse {
	private String planId;
	private String code;
	private String name;
	private Long price;
	private String duration;
	private String features;
	private String status;
	private Integer users;
	private String currency;

	// Constructor without users count (7 parameters)
	public SubscriptionPlanResponse(String planId, String code, String name, Long price, Integer durationDays,
			Boolean isActive, String currency) {
		this.planId = planId;
		this.code = code;
		this.name = name;
		this.price = price;
		this.currency = currency;
		this.duration = convertDuration(durationDays);
		this.status = isActive ? "ACTIVE" : "INACTIVE";
		this.features = getDefaultFeatures(code, name);
		this.users = 0;
	}

	// NEW Constructor with users count (8 parameters)
	public SubscriptionPlanResponse(String planId, String code, String name, Long price, Integer durationDays,
			Boolean isActive, String currency, Integer users) {
		this.planId = planId;
		this.code = code;
		this.name = name;
		this.price = price;
		this.currency = currency;
		this.duration = convertDuration(durationDays);
		this.status = isActive ? "ACTIVE" : "INACTIVE";
		this.features = getDefaultFeatures(code, name);
		this.users = users != null ? users : 0;
	}

	private String convertDuration(Integer durationDays) {
		if (durationDays == null)
			return "Not specified";
		int months = durationDays / 30;
		int years = durationDays / 365;

		if (years >= 1 && durationDays % 365 == 0) {
			return years + (years > 1 ? " years" : " year");
		} else if (months >= 1 && durationDays % 30 == 0) {
			return months + (months > 1 ? " months" : " month");
		} else {
			return durationDays + (durationDays > 1 ? " days" : " day");
		}
	}

	private String getDefaultFeatures(String code, String name) {
		if (code != null && code.toUpperCase().contains("PREMIUM")) {
			return "Unlimited matches, Advanced filters, Priority support";
		} else if (code != null && code.toUpperCase().contains("GOLD")) {
			return "Limited matches, Basic filters, Email support";
		}
		return "Standard features included";
	}

	// Getters and Setters
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUsers() {
		return users;
	}

	public void setUsers(Integer users) {
		this.users = users;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public SubscriptionPlanResponse() {
		super();
	}
}