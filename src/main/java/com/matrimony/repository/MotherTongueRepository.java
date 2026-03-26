package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.matrimony.model.entity.MotherTongue;

public interface MotherTongueRepository extends JpaRepository<MotherTongue, Long> {
    MotherTongue findByLanguageName(String name);
}
