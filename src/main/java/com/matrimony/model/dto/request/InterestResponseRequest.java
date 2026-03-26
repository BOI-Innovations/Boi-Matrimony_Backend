package com.matrimony.model.dto.request;

import com.matrimony.model.enums.InterestStatus;

import jakarta.validation.constraints.NotNull;


public class InterestResponseRequest {
    
    @NotNull(message = "Interest status is required")
    private InterestStatus status;

    // Getters and Setters
    public InterestStatus getStatus() {
        return status;
    }

    public void setStatus(InterestStatus status) {
        this.status = status;
    }
}