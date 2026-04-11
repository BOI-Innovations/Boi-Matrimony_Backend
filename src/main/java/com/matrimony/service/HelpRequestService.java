package com.matrimony.service;

import java.time.LocalDate;

import com.matrimony.model.dto.request.HelpRequestRequest;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.enums.HelpRequestStatus;

public interface HelpRequestService {

	ResponseEntity createHelpRequest(HelpRequestRequest request);

	ResponseEntity getMyHelpRequests();

	ResponseEntity getHelpRequestById(Long id);

	ResponseEntity deleteHelpRequest(Long id);

	ResponseEntity getAllHelpRequests();

	ResponseEntity updateStatus(Long id, HelpRequestStatus status);

	ResponseEntity getHelpRequests(String search, int page, int limit, LocalDate fromDate, LocalDate toDate);
}
