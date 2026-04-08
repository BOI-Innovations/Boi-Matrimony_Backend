package com.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Location;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.User;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	Optional<Location> findByProfile(Profile profile);

	@Modifying
	@Query("DELETE FROM Location l WHERE l.profile.id = :profileId")
	void deleteByProfileId(@Param("profileId") Long profileId);

	boolean existsByProfile(Profile profile);

	@Query("SELECT l.state, COUNT(l) FROM Location l " + "WHERE l.profile.createdAt BETWEEN :fromDate AND :toDate "
			+ "GROUP BY l.state ORDER BY COUNT(l) DESC")
	List<Object[]> countByStateAndProfileCreatedAtBetween(@Param("fromDate") LocalDateTime fromDate,
			@Param("toDate") LocalDateTime toDate);

	@Query("SELECT l.state, COUNT(l) FROM Location l " + "WHERE l.profile.createdAt >= :fromDate "
			+ "GROUP BY l.state ORDER BY COUNT(l) DESC")
	List<Object[]> countByStateAndProfileCreatedAtAfter(@Param("fromDate") LocalDateTime fromDate);

	@Query("SELECT l.state, COUNT(l) FROM Location l " + "WHERE l.profile.createdAt <= :toDate "
			+ "GROUP BY l.state ORDER BY COUNT(l) DESC")
	List<Object[]> countByStateAndProfileCreatedAtBefore(@Param("toDate") LocalDateTime toDate);

	@Query("SELECT l.state, COUNT(l) FROM Location l " + "GROUP BY l.state ORDER BY COUNT(l) DESC")
	List<Object[]> countByStateGrouped();
	
}
