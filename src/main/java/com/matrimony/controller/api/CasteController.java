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

import com.matrimony.model.dto.request.CasteRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.CasteService;

@RestController
@RequestMapping("/api/castes")
@CrossOrigin(allowedHeaders = "*")
public class CasteController {

	@Autowired
	private CasteService casteService;

	@PostMapping
	public ResponseEntity createOrUpdateCaste(@RequestBody CasteRequest request) {
		return casteService.createOrUpdateCaste(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity getCasteById(@PathVariable Long id) {
		return casteService.getCasteById(id);
	}

	@GetMapping
	public ResponseEntity getAllCastes() {
		return casteService.getAllCastes();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteCaste(@PathVariable Long id) {
		return casteService.deleteCaste(id);
	}

	@PostMapping("/upload")
	public ResponseEntity uploadCasteExcel(@RequestParam("file") MultipartFile file) {
		return casteService.uploadCasteExcel(file);
	}
}
