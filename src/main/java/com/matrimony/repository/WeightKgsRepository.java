package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matrimony.model.entity.WeightKgs;

public interface WeightKgsRepository extends JpaRepository<WeightKgs, Long> {
	WeightKgs findByWeightInKgs(String weightInKgs);
}
