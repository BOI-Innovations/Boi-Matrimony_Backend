package com.matrimony.repository;

import com.matrimony.model.entity.OccupationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OccupationOptionRepository extends JpaRepository<OccupationOption, Long> {
    List<OccupationOption> findByCategoryId(Long categoryId);
}
