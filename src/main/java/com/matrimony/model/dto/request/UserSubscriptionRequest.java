package com.matrimony.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserSubscriptionRequest {

	@NotNull(message = "Subscription plan ID is required")
	private String planId;

	@NotBlank(message = "Payment ID is required")
	private String paymentId;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

}