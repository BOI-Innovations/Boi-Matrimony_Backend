package com.matrimony.service;

import com.matrimony.model.dto.request.ContactDetailsRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface ContactDetailsService {
    ResponseEntity getContactDetailsForCurrentUser();
    ResponseEntity createContactDetailsForCurrentUser(ContactDetailsRequest request);
    ResponseEntity updateContactDetailsForCurrentUser(ContactDetailsRequest request);
    ResponseEntity deleteContactDetailsForCurrentUser();
	ResponseEntity getContactDetailsById(Long id);
	ResponseEntity updateContactDetailsById(Long id, ContactDetailsRequest request);
	ResponseEntity deleteContactDetailsById(Long id);
}
