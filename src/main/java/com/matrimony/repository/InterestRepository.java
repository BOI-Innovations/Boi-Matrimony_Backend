package com.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Interest;
import com.matrimony.model.entity.User;
import com.matrimony.model.enums.InterestStatus;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
	List<Interest> findByFromUser(User fromUser);

	List<Interest> findByToUser(User toUser);

	Optional<Interest> findByFromUserAndToUser(User fromUser, User toUser);

	boolean existsByFromUserAndToUser(User fromUser, User toUser);

	long countByToUserAndStatus(User toUser, InterestStatus status);

	@Query("SELECT i FROM Interest i WHERE " + "i.fromUser.id = :userId AND i.status = 'ACCEPTED' AND "
			+ "EXISTS (SELECT i2 FROM Interest i2 WHERE i2.fromUser.id = i.toUser.id AND i2.toUser.id = i.fromUser.id AND i2.status = 'ACCEPTED')")
	List<Interest> findMutualInterests(@Param("userId") Long userId);
}