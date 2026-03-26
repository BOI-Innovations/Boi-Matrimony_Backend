package com.matrimony.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "created_for")
public class CreatedFor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String targetPerson;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTargetPerson() {
		return targetPerson;
	}

	public void setTargetPerson(String targetPerson) {
		this.targetPerson = targetPerson;
	}

}
