package com.matrimony.model.dto.response;

import com.matrimony.model.enums.FamilyStatus;
import com.matrimony.model.enums.FamilyType;
import com.matrimony.model.enums.FamilyValue;

public class FamilyDetailsResponse {
	private Long id;
	private FamilyValue familyValue;
	private FamilyType familyType;
	private FamilyStatus familyStatus;
	private String fatherOccupation;
	private String motherOccupation;
	private String nativePlace;
	private Integer noOfBrothers;
	private Integer brothersMarried;
	private Integer noOfSisters;
	private Integer sistersMarried;
	private String parentsContactNo;
	private String aboutMyFamily;
	private String grandFatherOccupation;
	private String grandMotherOccupation;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

}
