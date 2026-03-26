package com.matrimony.repository;

import com.matrimony.model.entity.OccupationCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OccupationCategoryRepository extends JpaRepository<OccupationCategory, Long> {
    List<OccupationCategory> findAll(Sort sort);
}
