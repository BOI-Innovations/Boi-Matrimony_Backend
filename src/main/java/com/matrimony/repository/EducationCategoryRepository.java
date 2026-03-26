package com.matrimony.repository;

import com.matrimony.model.entity.EducationCategory;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationCategoryRepository extends JpaRepository<EducationCategory, Long> {
	List<EducationCategory> findAll(Sort sort);
}
