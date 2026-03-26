package com.matrimony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Location;
import com.matrimony.model.entity.Profile;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByProfile(Profile profile);

    @Modifying
    @Query("DELETE FROM Location l WHERE l.profile.id = :profileId")
    void deleteByProfileId(@Param("profileId") Long profileId);

    boolean existsByProfile(Profile profile);
}
