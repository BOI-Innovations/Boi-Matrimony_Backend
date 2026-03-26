package com.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Block;
import com.matrimony.model.entity.User;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
	Optional<Block> findByBlockerAndBlocked(User blocker, User blocked);

	List<Block> findByBlocker(User blocker);

	List<Block> findByBlocked(User blocked);

	boolean existsByBlockerAndBlocked(User blocker, User blocked);

	@Query("SELECT b.blocked FROM Block b WHERE b.blocker = :blocker")
	List<User> findBlockedUsers(@Param("blocker") User blocker);

	@Query("SELECT b.blocker FROM Block b WHERE b.blocked = :blocked")
	List<User> findBlockedByUsers(@Param("blocked") User blocked);

	@Modifying
	@Query("DELETE FROM Block b WHERE b.blocker = :blocker AND b.blocked = :blocked")
	void unblockUser(@Param("blocker") User blocker, @Param("blocked") User blocked);

	@Modifying
	@Query("DELETE FROM Block b WHERE b.blocker = :user OR b.blocked = :user")
	void deleteAllUserBlocks(@Param("user") User user);

	@Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END "
			+ "FROM Block b WHERE (b.blocker = :user1 AND b.blocked = :user2) OR "
			+ "(b.blocker = :user2 AND b.blocked = :user1)")
	Boolean isBlocked(@Param("user1") User user1, @Param("user2") User user2);
}