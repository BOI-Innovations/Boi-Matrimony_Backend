package com.matrimony.service;

import org.springframework.web.multipart.MultipartFile;

import com.matrimony.exception.GenericException;
import com.matrimony.model.dto.request.SponsoredAdRequestDTO;
import com.matrimony.model.entity.ResponseEntity;

public interface SponsoredAdService {

	// CREATE sponsored ad (image / video)
	ResponseEntity createAd(SponsoredAdRequestDTO requestDTO, MultipartFile mediaFile);

	// GET ad by id
	ResponseEntity getAdById(Long id);

	// GET all active sponsored ads (ordered)
	ResponseEntity getAllActiveAds();

	// UPDATE sponsored ad
	ResponseEntity updateAd(Long id, SponsoredAdRequestDTO requestDTO, MultipartFile mediaFile);

	// DELETE sponsored ad
	ResponseEntity deleteAd(Long id);

	// SERVE image/video securely
	byte[] getAdMediaByPath(String mediaPath) throws GenericException;
}
