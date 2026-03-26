package com.matrimony.service;

import com.matrimony.model.dto.request.InterestRequest;
import com.matrimony.model.dto.response.InterestResponse;

import java.util.List;

public interface InterestService {
    InterestResponse sendInterest(InterestRequest interestRequest);
    InterestResponse respondToInterest(Long interestId, String status);
    void withdrawInterest(Long interestId);
    List<InterestResponse> getSentInterests();
    List<InterestResponse> getReceivedInterests();
    List<InterestResponse> getMutualInterests();
    boolean hasSentInterest(Long fromUserId, Long toUserId);
}