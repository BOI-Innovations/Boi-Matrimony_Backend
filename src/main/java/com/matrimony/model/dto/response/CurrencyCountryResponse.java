package com.matrimony.model.dto.response;

public class CurrencyCountryResponse {
	private Long id;
	private String currencyCountryName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrencyCountryName() {
		return currencyCountryName;
	}

	public void setCurrencyCountryName(String currencyCountryName) {
		this.currencyCountryName = currencyCountryName;
	}
}
