package com.matrimony.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.dto.request.SearchRequest;
import com.matrimony.model.dto.response.ProfileResponse;
import com.matrimony.model.dto.response.SearchResponse;
import com.matrimony.service.SearchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(allowedHeaders = "*")
public class SearchController {

	@Autowired
	private SearchService searchService;

	@PostMapping("/profiles")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<SearchResponse> searchProfiles(@Valid @RequestBody SearchRequest searchRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		SearchResponse response = searchService.searchProfiles(searchRequest, page, size);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/matches")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<ProfileResponse>> getMatches() {
		List<ProfileResponse> matches = searchService.getMatches();
		return ResponseEntity.ok(matches);
	}

	@GetMapping("/suggestions")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<ProfileResponse>> getSuggestions() {
		List<ProfileResponse> suggestions = searchService.getSuggestions();
		return ResponseEntity.ok(suggestions);
	}

	@GetMapping("/nearby")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<SearchResponse> getNearbyProfiles(@RequestParam String city,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		SearchResponse response = searchService.getNearbyProfiles(city, page, size);
		return ResponseEntity.ok(response);
	}

}