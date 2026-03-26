package com.matrimony.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "annual_income")
public class AnnualIncomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "income_range", nullable = false)
    private String incomeRange;

    public AnnualIncomeEntity() {}

    public AnnualIncomeEntity(String incomeRange) {
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
