	package com.matrimony.service;
	
	import com.matrimony.model.dto.request.FamilyDetailsRequest;
	import com.matrimony.model.entity.ResponseEntity;
	
	public interface FamilyDetailsService {
	
	    ResponseEntity getFamilyDetailsForCurrentUser();
	
	    ResponseEntity createFamilyDetailsForCurrentUser(FamilyDetailsRequest request);
	
	    ResponseEntity updateFamilyDetailsForCurrentUser(FamilyDetailsRequest request);
	
	    ResponseEntity deleteFamilyDetailsForCurrentUser();

		ResponseEntity updateParentsContactNumber(String contactDetails);
	}
