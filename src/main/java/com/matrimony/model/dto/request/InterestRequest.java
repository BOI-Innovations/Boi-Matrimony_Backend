package com.matrimony.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InterestRequest {
    
    @NotNull(message = "Receiver user ID is required")
    private Long toUserId;
    
    @Size(max = 500, message = "Message must be at most 500 characters")
    private String message;

    // Getters and Setters
    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}