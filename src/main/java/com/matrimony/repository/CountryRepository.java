package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
	Country findByCountryName(String countryName);
}