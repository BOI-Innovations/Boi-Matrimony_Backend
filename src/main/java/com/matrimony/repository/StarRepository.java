package com.matrimony.repository;

import com.matrimony.model.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {
    Star findByStarName(String name);
    boolean existsByStarNameIgnoreCase(String name);
}
