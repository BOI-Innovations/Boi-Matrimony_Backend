package com.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matrimony.model.entity.SponsoredAd;

public interface SponsoredAdRepository extends JpaRepository<SponsoredAd, Long> {

	Optional<SponsoredAd> findById(Long id);

	List<SponsoredAd> findByIsActive(boolean isActive);

	List<SponsoredAd> findByTitleContainingIgnoreCase(String title);

	List<SponsoredAd> findByIsActiveTrueOrderByDisplayOrderAsc();
}
