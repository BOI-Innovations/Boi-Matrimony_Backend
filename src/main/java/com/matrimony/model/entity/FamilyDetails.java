package com.matrimony.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrimony.model.enums.FamilyStatus;
import com.matrimony.model.enums.FamilyType;
import com.matrimony.model.enums.FamilyValue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "family_details")
public class FamilyDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id", nullable = false) 
	@JsonIgnore
	private Profile profile;

	private String parentsContactNo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FamilyValue familyValue;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FamilyType familyType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FamilyStatus familyStatus;

	private String fatherOccupation;

	private String motherOccupation;

	private String grandFatherOccupation;
	private String grandMotherOccupation;

	private String nativePlace;

	private Integer noOfBrothers;
	private Integer brothersMarried;

	private Integer noOfSisters;
	private Integer sistersMarried;

	@Column(length = 1000)
	private String aboutMyFamily;

	// ===== Getters and Setters =====
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getParentsContactNo() {
		return parentsContactNo;
	}

	public void setParentsContactNo(String parentsContactNo) {
		this.parentsContactNo = parentsContactNo;
	}

	public FamilyValue getFamilyValue() {
		return familyValue;
	}

	public void setFamilyValue(FamilyValue familyValue) {
		this.familyValue = familyValue;
	}

	public FamilyType getFamilyType() {
		return familyType;
	}

	public void setFamilyType(FamilyType familyType) {
		this.familyType = familyType;
	}

	public FamilyStatus getFamilyStatus() {
		return familyStatus;
	}

	public void setFamilyStatus(FamilyStatus familyStatus) {
		this.familyStatus = familyStatus;
	}

	public String getFatherOccupation() {
		return fatherOccupation;
	}

	public void setFatherOccupation(String fatherOccupation) {
		this.fatherOccupation = fatherOccupation;
	}

	public String getMotherOccupation() {
		return motherOccupation;
	}

	public void setMotherOccupation(String motherOccupation) {
		this.motherOccupation = motherOccupation;
	}

	public String getGrandFatherOccupation() {
		return grandFatherOccupation;
	}

	public void setGrandFatherOccupation(String grandFatherOccupation) {
		this.grandFatherOccupation = grandFatherOccupation;
	}

	public String getGrandMotherOccupation() {
		return grandMotherOccupation;
	}

	public void setGrandMotherOccupation(String grandMotherOccupation) {
		this.grandMotherOccupation = grandMotherOccupation;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public Integer getNoOfBrothers() {
		return noOfBrothers;
	}

	public void setNoOfBrothers(Integer noOfBrothers) {
		this.noOfBrothers = noOfBrothers;
	}

	public Integer getBrothersMarried() {
		return brothersMarried;
	}

	public void setBrothersMarried(Integer brothersMarried) {
		this.brothersMarried = brothersMarried;
	}

	public Integer getNoOfSisters() {
		return noOfSisters;
	}

	public void setNoOfSisters(Integer noOfSisters) {
		this.noOfSisters = noOfSisters;
	}

	public Integer getSistersMarried() {
		return sistersMarried;
	}

	public void setSistersMarried(Integer sistersMarried) {
		this.sistersMarried = sistersMarried;
	}

	public String getAboutMyFamily() {
		return aboutMyFamily;
	}

	public void setAboutMyFamily(String aboutMyFamily) {
		this.aboutMyFamily = aboutMyFamily;
	}
}