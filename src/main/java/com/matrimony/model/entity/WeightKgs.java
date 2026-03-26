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
@Table(name = "weight_kgs")
public class WeightKgs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Weight cannot be blank")
	@Pattern(regexp = "^[1-9][0-9]{1,2}\\s?kg$", message = "Weight must be like '41 kg', '70 kg', '120 kg'")
	@Column(nullable = false, unique = true, length = 10)
	private String weightInKgs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWeightInKgs() {
		return weightInKgs;
	}

	public void setWeightInKgs(String weightInKgs) {
		this.weightInKgs = weightInKgs;
	}

}
