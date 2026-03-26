package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.matrimony.model.entity.CreatedFor;

public interface CreatedForRepository extends JpaRepository<CreatedFor, Long> {
    CreatedFor findByTargetPerson(String targetPerson);
}
