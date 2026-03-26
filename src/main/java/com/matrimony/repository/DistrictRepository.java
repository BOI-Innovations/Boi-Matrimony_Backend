package com.matrimony.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    District findByNameAndStateId(String name, Long stateId);

	List<District> findByStateId(Long stateId);
}