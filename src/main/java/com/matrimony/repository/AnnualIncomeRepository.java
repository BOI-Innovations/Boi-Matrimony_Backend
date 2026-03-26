package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.matrimony.model.entity.AnnualIncomeEntity;

@Repository
public interface AnnualIncomeRepository extends JpaRepository<AnnualIncomeEntity, Long> {
}
