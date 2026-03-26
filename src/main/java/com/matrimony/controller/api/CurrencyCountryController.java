package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.CurrencyCountryRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.CurrencyCountryService;

@RestController
@RequestMapping("/api/currency-country")
@CrossOrigin(allowedHeaders = "*")
public class CurrencyCountryController {

	@Autowired
	private CurrencyCountryService service;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity createOrUpdate(@RequestBody CurrencyCountryRequest request) {
		return service.createOrUpdateCurrencyCountry(request);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity getById(@PathVariable Long id) {
		return service.getCurrencyCountryById(id);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity getAll() {
		return service.getAllCurrencyCountries();
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity deleteById(@PathVariable Long id) {
		return service.deleteCurrencyCountry(id);
	}

	@PostMapping("/upload")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
		return service.uploadCurrencyCountryExcel(file);
	}
}
