package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.CountryRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.CountryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/country")
@CrossOrigin(allowedHeaders = "*")
public class CountryController {

	@Autowired
	private CountryService countryService;

	@PostMapping
	public ResponseEntity create(@Valid @RequestBody CountryRequest request) {
		return countryService.createCountry(request);
	}

	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody CountryRequest request) {
		return countryService.updateCountry(id, request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return countryService.getCountryById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return countryService.getAllCountries();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		return countryService.deleteCountry(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
		return countryService.uploadCountryExcel(file);
	}
}
