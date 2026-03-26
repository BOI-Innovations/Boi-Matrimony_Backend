package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.SubCaste;

@Repository
public interface SubCasteRepository extends JpaRepository<SubCaste, Long> {
    SubCaste findBySubCasteName(String name);
}
