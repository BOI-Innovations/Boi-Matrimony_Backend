package com.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.GalleryImage;

@Repository
public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {
//	List<ProfileImage> findByProfile(Profile profile);
//
//	Optional<ProfileImage> findByProfileAndIsPrimary(Profile profile, Boolean isPrimary);
//
//	long countByProfile(Profile profile);
//
//	List<ProfileImage> findByProfileIdOrderByIsPrimaryDesc(Long profileId);
//
//	@Modifying
//	@Query("UPDATE ProfileImage p SET p.isPrimary = false WHERE p.profile.id = :profileId")
//	void resetPrimaryImages(@Param("profileId") Long profileId);
//
//	@Modifying
//	@Query("UPDATE ProfileImage p SET p.isPrimary = true WHERE p.id = :imageId")
//	void setAsPrimaryImage(@Param("imageId") Long imageId);
//	
//	@Modifying
//	@Query("DELETE FROM ProfileImage p WHERE p.profile.id = :profileId")
//	void deleteAllByProfileId(@Param("profileId") Long profileId);
//
//	@Query("SELECT p FROM ProfileImage p WHERE p.profile.id = :profileId ORDER BY p.uploadedAt DESC")
//	List<ProfileImage> findLatestImages(@Param("profileId") Long profileId);

	Optional<GalleryImage> findByIdAndProfileId(Long imageId, Long id);

	@Query("SELECT p.imageUrl FROM GalleryImage p WHERE p.id = :id")
	String findImageUrlById(@Param("id") Long id);

	List<GalleryImage> getProfileImageByProfileId(Long profileId);

	List<GalleryImage> findByProfileId(Long profileId);

}