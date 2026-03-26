package com.matrimony.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
	State findByNameAndCountryId(String name, Long countryId);

	List<State> findByCountryId(Long countryId);
}