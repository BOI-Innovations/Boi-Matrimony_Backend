package com.matrimony.repository;

import com.matrimony.model.entity.CommunicationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunicationSettingsRepository extends JpaRepository<CommunicationSettings, Long> {
    Optional<CommunicationSettings> findByUserId(Long userId);
	CommunicationSettings getCommunicationSettingsByUserId(Long userId);
	boolean existsByUserId(Long userId);
}
