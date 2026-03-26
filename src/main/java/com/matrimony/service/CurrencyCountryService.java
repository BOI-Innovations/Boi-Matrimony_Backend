package com.matrimony.service;

import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.CurrencyCountryRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface CurrencyCountryService {
	ResponseEntity createOrUpdateCurrencyCountry(CurrencyCountryRequest request);

	ResponseEntity getCurrencyCountryById(Long id);

	ResponseEntity getAllCurrencyCountries();

	ResponseEntity deleteCurrencyCountry(Long id);

	ResponseEntity uploadCurrencyCountryExcel(MultipartFile file);
}
