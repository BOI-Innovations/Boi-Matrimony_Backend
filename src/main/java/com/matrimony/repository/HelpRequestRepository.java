package com.matrimony.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matrimony.model.entity.HelpRequest;
import com.matrimony.model.entity.User;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
	List<HelpRequest> findByUser(User user);

	Long countByStatus(String status);
}
