package com.matrimony.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.matrimony.model.dto.request.AnnualIncomeRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.AnnualIncomeService;

@RestController
@RequestMapping("/api/annual-income")
@CrossOrigin(allowedHeaders = "*")
public class AnnualIncomeController {

	@Autowired
	private AnnualIncomeService service;

	@PostMapping
	public ResponseEntity createOrUpdate(@RequestBody AnnualIncomeRequest request) {
		return service.createOrUpdateAnnualIncome(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable Long id) {
		return service.getAnnualIncomeById(id);
	}

	@GetMapping
	public ResponseEntity getAll() {
		return service.getAllAnnualIncomes();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteById(@PathVariable Long id) {
		return service.deleteAnnualIncome(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadExcel(@RequestParam("file") MultipartFile file) {
		return service.uploadAnnualIncomeExcel(file);
	}
}
