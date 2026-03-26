package com.matrimony.repository;

import com.matrimony.model.entity.EducationOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationOptionRepository extends JpaRepository<EducationOption, Long> {

    List<EducationOption> findByCategoryId(Long categoryId);
}
