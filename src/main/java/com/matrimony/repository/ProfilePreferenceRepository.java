package com.matrimony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ProfilePreference;

@Repository
public interface ProfilePreferenceRepository extends JpaRepository<ProfilePreference, Long> {

	Optional<ProfilePreference> findByProfile(Profile profile);

	Optional<ProfilePreference> findByProfileId(Long profileId);
}