package com.matrimony.service;

import com.matrimony.model.dto.request.HelpRequestRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface HelpRequestService {

	ResponseEntity createHelpRequest(HelpRequestRequest request);

	ResponseEntity getMyHelpRequests();

	ResponseEntity getHelpRequestById(Long id);

	ResponseEntity updateStatus(Long id, String status);

	ResponseEntity deleteHelpRequest(Long id);

	ResponseEntity getAllHelpRequests();
}
