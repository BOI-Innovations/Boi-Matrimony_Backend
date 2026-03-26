package com.matrimony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.ContactDetails;
import com.matrimony.model.entity.User;

@Repository
public interface ContactDetailsRepository extends JpaRepository<ContactDetails, Long> {
	Optional<ContactDetails> findByUser(User user);

	boolean existsByUser(User user);
}
