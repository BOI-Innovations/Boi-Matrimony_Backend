package com.matrimony.service;

import com.matrimony.model.dto.request.CountryRequest;
import com.matrimony.model.entity.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CountryService {

    ResponseEntity createCountry(CountryRequest request);

    ResponseEntity updateCountry(Long id, CountryRequest request);

    ResponseEntity getCountryById(Long id);

    ResponseEntity getAllCountries();

    ResponseEntity deleteCountry(Long id);

    ResponseEntity uploadCountryExcel(MultipartFile file);
}
