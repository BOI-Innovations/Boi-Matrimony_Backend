package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matrimony.model.entity.CurrencyCountry;

public interface CurrencyCountryRepository extends JpaRepository<CurrencyCountry, Long> {
	CurrencyCountry findByCurrencyCountryName(String currencyCountryName);
}
