package com.matrimony.repository;

import com.matrimony.model.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomSearchRepository {
    
    Page<Profile> searchProfilesWithFilters(
            String religion,
            String gender,
            Integer minAge,
            Integer maxAge,
            String maritalStatus,
            String caste,
            String education,
            String occupation,
            Double minIncome,
            String city,
            String country,
            Pageable pageable);
    
    Page<Profile> searchProfilesByPreferences(
            Long profileId,
            Pageable pageable);
}