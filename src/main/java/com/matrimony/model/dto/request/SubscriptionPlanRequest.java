package com.matrimony.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SubscriptionPlanRequest {
    
    @NotBlank(message = "Code is required")
    @Size(min = 3, max = 20, message = "Code must be between 3 and 20 characters")
    private String code;
    
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Long price;
    
    @NotNull(message = "Duration days is required")
    @Min(value = 1, message = "Duration days must be at least 1")
    private Integer durationDays;
    
    private Boolean isActive = true;
    
    private String currency = "INR";
    
    // Getters and Setters
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getPrice() {
        return price;
    }
    
    public void setPrice(Long price) {
        this.price = price;
    }
    
    public Integer getDurationDays() {
        return durationDays;
    }
    
    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}