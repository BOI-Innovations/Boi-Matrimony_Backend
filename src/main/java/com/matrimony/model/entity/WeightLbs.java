package com.matrimony.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "weight_lbs")
public class WeightLbs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Weight cannot be blank")
    @Pattern(regexp = "^[1-9][0-9]{1,3}\\s?lbs$", message = "Weight must be like '90 lbs', '150 lbs', '250 lbs'")
	@Column(name = "weight_in_lbs", nullable = false, unique = true)
	private String weightInLbs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWeightInLbs() {
		return weightInLbs;
	}

	public void setWeightInLbs(String weightInLbs) {
		this.weightInLbs = weightInLbs;
	}
}
