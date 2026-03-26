package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.matrimony.model.entity.WeightLbs;

public interface WeightLbsRepository extends JpaRepository<WeightLbs, Long> {
    WeightLbs findByWeightInLbs(String weightInLbs);
}
