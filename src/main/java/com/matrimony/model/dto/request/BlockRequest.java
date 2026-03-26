package com.matrimony.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BlockRequest {
    
    @NotNull(message = "User ID to block is required")
    private Long userIdToBlock;
    
    @Size(max = 500, message = "Reason must be at most 500 characters")
    private String reason;

    // Getters and Setters
    public Long getUserIdToBlock() {
        return userIdToBlock;
    }

    public void setUserIdToBlock(Long userIdToBlock) {
        this.userIdToBlock = userIdToBlock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}