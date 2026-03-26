package com.matrimony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.FamilyDetails;
import com.matrimony.model.entity.Profile;

@Repository
public interface FamilyDetailsRepository extends JpaRepository<FamilyDetails, Long> {

	Optional<FamilyDetails> findByProfile(Profile profile);

	@Modifying
	@Query("DELETE FROM FamilyDetails f WHERE f.profile.id = :profileId")
	void deleteByProfileId(@Param("profileId") Long profileId);

	boolean existsByProfile(Profile profile);
}
