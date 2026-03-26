package com.matrimony.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class WeightLbsRequest {

    @NotBlank(message = "Weight is required")
    @Pattern(regexp = "^[1-9][0-9]{1,3}\\s?lbs$", message = "Weight must be like '90 lbs', '150 lbs', '250 lbs'")
    private String weightInLbs;

    public String getWeightInLbs() {
        return weightInLbs;
    }

    public void setWeightInLbs(String weightInLbs) {
        this.weightInLbs = weightInLbs;
    }
}
