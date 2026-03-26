package com.matrimony.service;

import com.matrimony.model.entity.ResponseEntity;

public interface ConnectionService {

    ResponseEntity sendRequest(Long receiverId);

    ResponseEntity accept(Long requestId);

    ResponseEntity reject(Long requestId);

    ResponseEntity getMyRequests();

    ResponseEntity getMyConnections();

	ResponseEntity getMySentRequests();

	ResponseEntity withdraw(Long requestId);
}
