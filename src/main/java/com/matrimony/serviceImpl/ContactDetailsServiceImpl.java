package com.matrimony.serviceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.ContactDetailsRequest;
import com.matrimony.model.dto.response.ContactDetailsResponse;
import com.matrimony.model.entity.ContactDetails;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.ContactDetailsRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.ContactDetailsService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ContactDetailsServiceImpl implements ContactDetailsService {

	@Autowired
	private ContactDetailsRepository contactDetailsRepository;

	@Autowired
	private UserRepository userRepository;

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

	@Override
	public ResponseEntity getContactDetailsForCurrentUser() {
		try {
			Long userId = getCurrentUserId();

			User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			if (user.getContactDetails() == null) {
				return new ResponseEntity("Contact details not found", 404, null);
			}
			ContactDetailsResponse contactResponse = convertToResponse(user.getContactDetails());
			String parentsContactNo = null;
			if (user.getProfile() != null && user.getProfile().getFamilyDetails() != null) {
				parentsContactNo = user.getProfile().getFamilyDetails().getParentsContactNo();
			}

			Map<String, Object> payload = new HashMap<>();
			payload.put("contactDetails", contactResponse);
			payload.put("parentsContactNo", parentsContactNo);

			return new ResponseEntity("Contact details fetched successfully", 200, payload);

		} catch (EntityNotFoundException ex) {
			return new ResponseEntity("User not found", 404, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity("Error fetching contact details: " + ex.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity createContactDetailsForCurrentUser(ContactDetailsRequest request) {
		try {
			Long userId = getCurrentUserId();
			User user = userRepository.findById(userId).orElse(null);

			if (user == null) {
				return new ResponseEntity("User not found", 404, null);
			}

			if (user.getContactDetails() != null) {
				return new ResponseEntity("Contact details already exist", 400, null);
			}

			ContactDetails contactDetails = new ContactDetails();
			mapRequestToContactDetails(request, contactDetails);
			contactDetails.setUser(user);
			contactDetailsRepository.save(contactDetails);

			user.setContactDetails(contactDetails);
			userRepository.save(user);

			return new ResponseEntity("Contact details created successfully", 201, convertToResponse(contactDetails));

		} catch (Exception e) {
			return new ResponseEntity("Error creating contact details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateContactDetailsForCurrentUser(ContactDetailsRequest request) {
		try {
			Long userId = getCurrentUserId();

			User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

			ContactDetails contactDetails = user.getContactDetails();
			boolean isNew = false;

			if (contactDetails == null) {
				contactDetails = new ContactDetails();
				contactDetails.setUser(user);
				isNew = true;
			}

			mapRequestToContactDetails(request, contactDetails);
			ContactDetails saved = contactDetailsRepository.save(contactDetails);

			if (isNew) {
				user.setContactDetails(saved);
				userRepository.save(user);
			}

			return new ResponseEntity(
					isNew ? "Contact details created successfully" : "Contact details updated successfully",
					isNew ? 201 : 200, convertToResponse(saved));

		} catch (EntityNotFoundException ex) {
			return new ResponseEntity("User not found", 404, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity("Error processing contact details: " + ex.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deleteContactDetailsForCurrentUser() {
		try {
			Long userId = getCurrentUserId();
			User user = userRepository.findById(userId).orElse(null);

			if (user == null || user.getContactDetails() == null) {
				return new ResponseEntity("Contact details not found", 404, null);
			}

			ContactDetails contactDetails = user.getContactDetails();
			user.setContactDetails(null);
			userRepository.save(user);

			contactDetailsRepository.delete(contactDetails);

			return new ResponseEntity("Contact details deleted successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting contact details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getContactDetailsById(Long id) {
		try {
			ContactDetails contactDetails = contactDetailsRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Contact details not found"));

			return new ResponseEntity("Contact details fetched successfully", 200, convertToResponse(contactDetails));

		} catch (EntityNotFoundException ex) {
			return new ResponseEntity("Contact details not found", 404, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity("Error fetching contact details: " + ex.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateContactDetailsById(Long id, ContactDetailsRequest request) {
		try {
			Optional<ContactDetails> contactOpt = contactDetailsRepository.findById(id);

			if (contactOpt.isEmpty()) {
				return new ResponseEntity("Contact details not found", 404, null);
			}

			ContactDetails contactDetails = contactOpt.get();
			mapRequestToContactDetails(request, contactDetails);
			contactDetailsRepository.save(contactDetails);

			return new ResponseEntity("Contact details updated successfully", 200, convertToResponse(contactDetails));
		} catch (Exception e) {
			return new ResponseEntity("Error updating contact details: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deleteContactDetailsById(Long id) {
		try {
			Optional<ContactDetails> contactOpt = contactDetailsRepository.findById(id);

			if (contactOpt.isEmpty()) {
				return new ResponseEntity("Contact details not found", 404, null);
			}

			ContactDetails contactDetails = contactOpt.get();

			User user = contactDetails.getUser();
			if (user != null) {
				user.setContactDetails(null);
				userRepository.save(user);
			}

			contactDetailsRepository.delete(contactDetails);

			return new ResponseEntity("Contact details deleted successfully", 200, null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting contact details: " + e.getMessage(), 500, null);
		}
	}

	private void mapRequestToContactDetails(ContactDetailsRequest request, ContactDetails contactDetails) {
		contactDetails.setMobileNumber(request.getMobileNumber());
		contactDetails.setEmail(request.getEmail());
	}

	private ContactDetailsResponse convertToResponse(ContactDetails contactDetails) {
		ContactDetailsResponse response = new ContactDetailsResponse();
		response.setId(contactDetails.getId());
		response.setMobileNumber(contactDetails.getMobileNumber());
		response.setEmail(contactDetails.getEmail());
		return response;
	}
}
