package com.matrimony.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currency_country")
public class CurrencyCountry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String currencyCountryName;

	public CurrencyCountry() {
	}

	public CurrencyCountry(String currencyCountryName) {
		this.currencyCountryName = currencyCountryName;
	}

	public Long getId() {
		return id;
	}

	public String getCurrencyCountryName() {
		return currencyCountryName;
	}

	public void setCurrencyCountryName(String currencyCountryName) {
		this.currencyCountryName = currencyCountryName;
	}
}
