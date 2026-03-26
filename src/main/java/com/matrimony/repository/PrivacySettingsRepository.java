package com.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.matrimony.model.entity.PrivacySettings;
import com.matrimony.model.entity.User;
import java.util.Optional;

public interface PrivacySettingsRepository extends JpaRepository<PrivacySettings, Long> {
    Optional<PrivacySettings> findByUser(User user);

	boolean existsByUserId(Long userId);

	PrivacySettings getPrivacySettingByUserId(Long userId);
}
