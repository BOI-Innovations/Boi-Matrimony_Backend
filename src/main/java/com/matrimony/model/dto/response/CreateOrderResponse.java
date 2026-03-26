package com.matrimony.model.dto.response;

import java.util.Map;

public class CreateOrderResponse {

	private String orderId;
	private Long amount;
	private String currency;
	private String status;
	private String receipt;
	private Map<String, Object> notes;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public Map<String, Object> getNotes() {
		return notes;
	}

	public void setNotes(Map<String, Object> notes) {
		this.notes = notes;
	}
}
