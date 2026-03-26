package com.matrimony.model.dto.response;

public class AnnualIncomeResponse {
    private Long id;
    private String incomeRange;

    public AnnualIncomeResponse() {}

    public AnnualIncomeResponse(Long id, String incomeRange) {
        this.id = id;
        this.incomeRange = incomeRange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIncomeRange() {
        return incomeRange;
    }

    public void setIncomeRange(String incomeRange) {
        this.incomeRange = incomeRange;
    }
}
