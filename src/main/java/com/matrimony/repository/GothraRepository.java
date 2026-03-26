package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Gothra;

@Repository
public interface GothraRepository extends JpaRepository<Gothra, Long> {
    Gothra findByGothraName(String name);
    boolean existsByGothraNameIgnoreCase(String name);
}
