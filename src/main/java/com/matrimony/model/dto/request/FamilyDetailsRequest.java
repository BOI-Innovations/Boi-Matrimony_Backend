package com.matrimony.model.dto.request;

import com.matrimony.model.enums.FamilyStatus;
import com.matrimony.model.enums.FamilyType;
import com.matrimony.model.enums.FamilyValue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FamilyDetailsRequest {

	@NotNull(message = "Family value is required")
	private FamilyValue familyValue;

	@NotNull(message = "Family type is required")
	private FamilyType familyType;

	@NotNull(message = "Family status is required")
	private FamilyStatus familyStatus;

	@NotBlank(message = "Father occupation is required")
	private String fatherOccupation;

	@NotBlank(message = "Mother occupation is required")
	private String motherOccupation;

	private String grandFatherOccupation;
	private String grandMotherOccupation;

	@NotBlank(message = "Native place is required")
	private String nativePlace;

	private Integer noOfBrothers;
	private Integer brothersMarried;
	private Integer noOfSisters;
	private Integer sistersMarried;

	private String parentsContactNo;
	private String aboutMyFamily;

	// ===== Getters and Setters =====
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

	public String getParentsContactNo() {
		return parentsContactNo;
	}

	public void setParentsContactNo(String parentsContactNo) {
		this.parentsContactNo = parentsContactNo;
	}

	public String getAboutMyFamily() {
		return aboutMyFamily;
	}

	public void setAboutMyFamily(String aboutMyFamily) {
		this.aboutMyFamily = aboutMyFamily;
	}
}