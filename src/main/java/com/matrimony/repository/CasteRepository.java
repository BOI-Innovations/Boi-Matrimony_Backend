package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Caste;

@Repository
public interface CasteRepository extends JpaRepository<Caste, Long> {
	Caste findByCasteName(String name);

}
