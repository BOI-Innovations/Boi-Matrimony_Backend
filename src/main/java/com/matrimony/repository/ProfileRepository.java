package com.matrimony.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.dto.request.ProfileFilterRequest;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.enums.DietaryHabits;
import com.matrimony.model.enums.DrinkingHabits;
import com.matrimony.model.enums.EmploymentType;
import com.matrimony.model.enums.Gender;
import com.matrimony.model.enums.Manglik;
import com.matrimony.model.enums.MaritalStatus;
import com.matrimony.model.enums.PhysicalStatus;
import com.matrimony.model.enums.ProfileVerificationStatus;
import com.matrimony.model.enums.Religion;
import com.matrimony.model.enums.SmokingHabits;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

	List<Profile> findByVerificationStatus(ProfileVerificationStatus status);

    @Query("SELECT COUNT(p) FROM Profile p WHERE p.verificationStatus = :status")
    Long countByVerificationStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(p) FROM Profile p WHERE p.profileComplete = true AND p.user.isActive = true")
    Long countActiveProfiles();
    
    @Query("SELECT COUNT(p) FROM Profile p WHERE p.gender = :gender")
    Long countByGender(@Param("gender") String gender);
    
	@Query("SELECT p FROM Profile p WHERE LOWER(p.location.city) = LOWER(:city)")
	Page<Profile> findByCityIgnoreCase(@Param("city") String city, Pageable pageable);

	long countByVerificationStatus(ProfileVerificationStatus status);

	@Query(value = "SELECT * FROM profiles p WHERE " + "(:gender IS NULL OR p.gender = :gender) AND "
			+ "(:minDob IS NULL OR p.date_of_birth <= :minDob) AND "
			+ "(:maxDob IS NULL OR p.date_of_birth >= :maxDob) AND "
			+ "(:religion IS NULL OR p.religion = :religion) AND " + "(:caste IS NULL OR p.caste = :caste) AND "
			+ "(:maritalStatus IS NULL OR p.marital_status = :maritalStatus) AND "
			+ "(:education IS NULL OR p.education LIKE CONCAT('%', :education, '%')) AND "
			+ "(:occupation IS NULL OR p.occupation LIKE CONCAT('%', :occupation, '%')) AND "
			+ "(:city IS NULL OR p.city = :city) AND "
			+ "(:country IS NULL OR p.country = :country)", nativeQuery = true)
	Page<Profile> searchProfiles(@Param("gender") Gender gender, @Param("minDob") Integer integer,
			@Param("maxDob") Integer integer2, @Param("religion") Religion religion, @Param("caste") String caste,
			@Param("maritalStatus") MaritalStatus maritalStatus, @Param("education") String education,
			@Param("occupation") String occupation, @Param("city") String city, @Param("country") String country,
			Pageable pageable);

	@Query("SELECT p FROM Profile p WHERE " + "p.gender <> :gender AND "
			+ "(:religion IS NULL OR p.religion = :religion) AND " + "(:caste IS NULL OR p.caste = :caste) AND "
			+ "(:maritalStatus IS NULL OR p.maritalStatus = :maritalStatus) AND "
			+ "p.verificationStatus = com.matrimony.model.enums.ProfileVerificationStatus.VERIFIED")
	List<Profile> findPotentialMatches(@Param("gender") Gender gender, @Param("religion") Religion religion,
			@Param("caste") String caste, @Param("maritalStatus") MaritalStatus maritalStatus, Pageable pageable);

	@Query("SELECT p FROM Profile p " + "LEFT JOIN p.location loc " + "LEFT JOIN p.educationOccupationDetails edu "
			+ "WHERE p.id <> :profileId " + "AND (:gender IS NULL OR p.gender <> :gender) "
			+ "AND (:city IS NULL OR loc.city = :city) "
			+ "AND (:education IS NULL OR edu.highestEducation LIKE CONCAT(:education, '%')) "
			+ "AND p.verificationStatus = com.matrimony.model.enums.ProfileVerificationStatus.VERIFIED "
			+ "ORDER BY p.profileCompletionPercentage DESC")
	List<Profile> findSuggestions(@Param("profileId") Long profileId, @Param("gender") Gender gender,
			@Param("city") String city, @Param("education") String education, Pageable pageable);

	// Find Profile along with FamilyDetails eagerly (optional)
	@Query("SELECT p FROM Profile p LEFT JOIN FETCH p.familyDetails WHERE p.user.id = :userId")
	Optional<Profile> findProfileWithFamilyDetailsByUserId(@Param("userId") Long userId);

	// Check if FamilyDetails exists for a given User ID
	@Query("SELECT CASE WHEN COUNT(p.familyDetails) > 0 THEN true ELSE false END FROM Profile p WHERE p.user.id = :userId")
	boolean existsFamilyDetailsByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT id FROM matrimony_db.profiles WHERE user_id = :userId LIMIT 1", nativeQuery = true)
	Long findProfileIdByUserId(@Param("userId") Long userId);

//	@Query("SELECT p FROM Profile p WHERE p.user.id != :userId AND p.profileComplete = true")
//	Page<Profile> findByUserIdNotAndProfileCompleteTrue(@Param("userId") Long userId, Pageable pageable);

//	@Query("""
//			SELECT p FROM Profile p
//			WHERE p.user.id != :userId
//			AND p.profileComplete = true
//			AND p.gender = :preferredGender
//			""")
//	Page<Profile> findByUserIdNotAndProfileCompleteTrueAndGender(@Param("userId") Long userId,
//			@Param("preferredGender") Gender preferredGender, Pageable pageable);
	@Query("""
			SELECT p FROM Profile p
			WHERE p.user.id != :userId
			AND p.profileComplete = true
			AND p.gender = :preferredGender
			AND NOT EXISTS (
			    SELECT ps FROM PrivacySettings ps
			    WHERE ps.user.id = p.user.id
			    AND (ps.hideProfile = true 
			         OR ps.profileVisibility = com.matrimony.model.enums.ProfileVisibility.NONE)
			)
			""")
			Page<Profile> findByUserIdNotAndProfileCompleteTrueAndGender(
			        @Param("userId") Long userId,
			        @Param("preferredGender") Gender preferredGender,
			        Pageable pageable);
	Optional<Profile> findByUserId(Long userId);

	@Query(value = "SELECT CONCAT(IFNULL(first_name, ''), ' ', IFNULL(last_name, '')) AS full_name "
			+ "FROM profiles WHERE user_id = :userId", nativeQuery = true)
	String findFullNameByUserId(@Param("userId") Long userId);

	@Query("SELECT p FROM Profile p WHERE p.user.id = :userId")
	Optional<Profile> findByUser_Id(@Param("userId") Long userId);

	@Query(value = "SELECT user_id FROM profiles WHERE id = :profileId", nativeQuery = true)
	Long findUserIdByProfileId(@Param("profileId") Long profileId);

	@Query("""
			    SELECT DISTINCT p FROM Profile p
			    LEFT JOIN FETCH p.location l
			    LEFT JOIN FETCH p.educationOccupationDetails e
			    LEFT JOIN FETCH p.hobbiesAndInterests h
			    LEFT JOIN FETCH p.familyDetails f
			    WHERE p.user.id != :currentUserId
			    AND p.profileComplete = true
			    AND p.gender = :gender

			    AND (:maxBirthDateForMinAge IS NULL OR p.dateOfBirth <= :maxBirthDateForMinAge)
			    AND (:minBirthDateForMaxAge IS NULL OR p.dateOfBirth >= :minBirthDateForMaxAge)

			    AND (:maritalStatus IS NULL OR p.maritalStatus = :maritalStatus)
			    AND (:religion IS NULL OR p.religion = :religion)
			    AND (:physicalStatus IS NULL OR p.physicalStatus = :physicalStatus)
			    AND (:dietaryHabits IS NULL OR p.dietaryHabits = :dietaryHabits)
			    AND (:smokingHabits IS NULL OR p.smokingHabits = :smokingHabits)
			    AND (:drinkingHabits IS NULL OR p.drinkingHabits = :drinkingHabits)
			    AND (:manglik IS NULL OR p.manglik = :manglik)

			    AND (:motherTongue IS NULL OR p.motherTongue = :motherTongue
			         OR EXISTS (SELECT 1 FROM p.languagesKnown lk WHERE lk = :motherTongue))

			    AND (:caste IS NULL OR p.caste = :caste)
			    AND (:subCaste IS NULL OR p.subCaste = :subCaste)
			    AND (:gothra IS NULL OR p.gothra = :gothra)
			    AND (:star IS NULL OR p.star = :star)
			    AND (:rashi IS NULL OR p.rashi = :rashi)

			    AND (:highestEducation IS NULL OR
			         EXISTS (SELECT 1 FROM EducationOccupationDetails e2
			                 WHERE e2.profile = p AND e2.highestEducation = :highestEducation))

			    AND (:employedIn IS NULL OR
			         EXISTS (SELECT 1 FROM EducationOccupationDetails e3
			                 WHERE e3.profile = p AND e3.employedIn = :employedIn))

			    AND (:occupation IS NULL OR
			         EXISTS (SELECT 1 FROM EducationOccupationDetails e4
			                 WHERE e4.profile = p AND e4.occupation LIKE %:occupation%))

			    AND (:country IS NULL OR
			         EXISTS (SELECT 1 FROM Location l2
			                 WHERE l2.profile = p AND l2.country = :country))

			    AND (:citizenship IS NULL OR
			         EXISTS (SELECT 1 FROM Location l3
			                 WHERE l3.profile = p AND l3.citizenship = :citizenship))

			    AND NOT EXISTS (
			        SELECT 1 FROM Connection c
			        WHERE
			            (
			                (c.sender.id = :currentUserId AND c.receiver.id = p.user.id)
			                OR
			                (c.receiver.id = :currentUserId AND c.sender.id = p.user.id)
			            )
			    )
			""")
	Page<Profile> findFilteredProfiles(@Param("currentUserId") Long currentUserId, @Param("gender") Gender gender,
			@Param("maxBirthDateForMinAge") LocalDate maxBirthDateForMinAge,
			@Param("minBirthDateForMaxAge") LocalDate minBirthDateForMaxAge,
			@Param("maritalStatus") MaritalStatus maritalStatus, @Param("religion") Religion religion,
			@Param("physicalStatus") PhysicalStatus physicalStatus, @Param("dietaryHabits") DietaryHabits dietaryHabits,
			@Param("smokingHabits") SmokingHabits smokingHabits, @Param("drinkingHabits") DrinkingHabits drinkingHabits,
			@Param("manglik") Manglik manglik, @Param("motherTongue") String motherTongue, @Param("caste") String caste,
			@Param("subCaste") String subCaste, @Param("gothra") String gothra, @Param("star") String star,
			@Param("rashi") String rashi, @Param("highestEducation") String highestEducation,
			@Param("employedIn") EmploymentType employedIn, @Param("occupation") String occupation,
			@Param("country") String country, @Param("citizenship") String citizenship, Pageable pageable);

	// Helper method to convert filter request to parameters
	default Page<Profile> findFilteredProfiles(Long currentUserId, Gender gender, ProfileFilterRequest filter,
			Pageable pageable) {
		// Convert enums safely
		MaritalStatus maritalStatus = filter.getMaritalStatus() != null
				? MaritalStatus.valueOf(filter.getMaritalStatus().toUpperCase())
				: null;
		Religion religion = filter.getReligion() != null ? Religion.valueOf(filter.getReligion().toUpperCase()) : null;
		PhysicalStatus physicalStatus = filter.getPhysicalStatus() != null
				? PhysicalStatus.valueOf(filter.getPhysicalStatus().toUpperCase())
				: null;
		DietaryHabits dietaryHabits = filter.getDietaryHabits() != null
				? DietaryHabits.valueOf(filter.getDietaryHabits().toUpperCase())
				: null;
		SmokingHabits smokingHabits = filter.getSmokingHabits() != null
				? SmokingHabits.valueOf(filter.getSmokingHabits().toUpperCase())
				: null;
		DrinkingHabits drinkingHabits = filter.getDrinkingHabits() != null
				? DrinkingHabits.valueOf(filter.getDrinkingHabits().toUpperCase())
				: null;
		Manglik manglik = filter.getManglik() != null ? Manglik.valueOf(filter.getManglik().toUpperCase()) : null;
		EmploymentType employedIn = filter.getEmployedIn() != null && !filter.getEmployedIn().isEmpty()
				? EmploymentType.valueOf(filter.getEmployedIn().iterator().next().toUpperCase())
				: null;

		// For collections, take first value (simplified - you might want to handle
		// multiple values differently)
		String motherTongue = filter.getMotherTongues() != null && !filter.getMotherTongues().isEmpty()
				? filter.getMotherTongues().iterator().next()
				: null;
		String caste = filter.getCastes() != null && !filter.getCastes().isEmpty()
				? filter.getCastes().iterator().next()
				: null;
		String subCaste = filter.getSubCastes() != null && !filter.getSubCastes().isEmpty()
				? filter.getSubCastes().iterator().next()
				: null;
		String gothra = filter.getGothras() != null && !filter.getGothras().isEmpty()
				? filter.getGothras().iterator().next()
				: null;
		String star = filter.getStars() != null && !filter.getStars().isEmpty() ? filter.getStars().iterator().next()
				: null;
		String rashi = filter.getRashis() != null && !filter.getRashis().isEmpty()
				? filter.getRashis().iterator().next()
				: null;
		String highestEducation = filter.getEducationLevels() != null && !filter.getEducationLevels().isEmpty()
				? filter.getEducationLevels().iterator().next()
				: null;
		String occupation = filter.getOccupations() != null && !filter.getOccupations().isEmpty()
				? filter.getOccupations().iterator().next()
				: null;
		String country = filter.getCountriesLivedIn() != null && !filter.getCountriesLivedIn().isEmpty()
				? filter.getCountriesLivedIn().iterator().next()
				: null;
		String citizenship = filter.getCitizenships() != null && !filter.getCitizenships().isEmpty()
				? filter.getCitizenships().iterator().next()
				: null;

		LocalDate maxBirthDateForMinAge = null;
		LocalDate minBirthDateForMaxAge = null;

		LocalDate today = LocalDate.now();

		if (filter.getMinAge() != null) {
			maxBirthDateForMinAge = today.minusYears(filter.getMinAge());
		}

		if (filter.getMaxAge() != null) {
			minBirthDateForMaxAge = today.minusYears(filter.getMaxAge() + 1).plusDays(1);
		}

		return findFilteredProfiles(currentUserId, gender, maxBirthDateForMinAge, minBirthDateForMaxAge, maritalStatus,
				religion, physicalStatus, dietaryHabits, smokingHabits, drinkingHabits, manglik, motherTongue, caste,
				subCaste, gothra, star, rashi, highestEducation, employedIn, occupation, country, citizenship,
				pageable);
	}

	Long countByGender(Gender gender);
}