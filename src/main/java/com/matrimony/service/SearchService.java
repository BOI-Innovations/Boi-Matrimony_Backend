package com.matrimony.service;

import com.matrimony.model.dto.request.SearchRequest;
import com.matrimony.model.dto.response.ProfileResponse;
import com.matrimony.model.dto.response.SearchResponse;

import java.util.List;

public interface SearchService {
    SearchResponse searchProfiles(SearchRequest searchRequest, int page, int size);
    List<ProfileResponse> getMatches();
    List<ProfileResponse> getSuggestions();
    SearchResponse getNearbyProfiles(String city, int page, int size); 
}