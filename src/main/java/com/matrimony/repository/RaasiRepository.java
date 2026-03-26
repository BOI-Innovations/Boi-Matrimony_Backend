package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.matrimony.model.entity.Raasi;

@Repository
public interface RaasiRepository extends JpaRepository<Raasi, Long> {
    Raasi findByName(String name);
}
