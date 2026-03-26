package com.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.User;
import com.matrimony.model.enums.RoleName;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("""
			SELECT u FROM User u
			LEFT JOIN FETCH u.profile p
			WHERE u.username = :username
			""")
	Optional<User> findByUsernameWithProfile(String username);

	Optional<User> findByEmail(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Optional<User> findByVerificationToken(String token);

	// Newly added methods from the second repository
	List<User> findByIsActive(Boolean isActive);

	List<User> findByEmailVerified(Boolean emailVerified);

	List<User> findByRolesContaining(RoleName role);

	Page<User> findByIsActive(Boolean isActive, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.lastLoginAt < :date")
	List<User> findInactiveUsersSince(@Param("date") LocalDateTime date);

	@Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
	List<User> findUsersRegisteredBetween(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	@Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
	Long countActiveUsers();

	@Modifying
	@Query("UPDATE User u SET u.isActive = :isActive WHERE u.id = :userId")
	void updateUserStatus(@Param("userId") Long userId, @Param("isActive") Boolean isActive);

	@Modifying
	@Query("UPDATE User u SET u.lastLoginAt = :lastLoginAt WHERE u.id = :userId")
	void updateLastLogin(@Param("userId") Long userId, @Param("lastLoginAt") LocalDateTime lastLoginAt);

	@Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
	List<User> searchUsers(@Param("keyword") String keyword);

	Object countByIsActive(boolean b);

	Optional<User> findByUsername(String username);

	@Query("select u.id from User u where u.username = :username")
	Long findUserIdByUsername(String username);

}
