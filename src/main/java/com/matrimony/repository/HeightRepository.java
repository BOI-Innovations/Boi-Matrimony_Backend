package com.matrimony.repository;

import com.matrimony.model.entity.Height;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeightRepository extends JpaRepository<Height, Long> {
    Height findByHeight(String name);
    boolean existsByHeightIgnoreCase(String name);
}
