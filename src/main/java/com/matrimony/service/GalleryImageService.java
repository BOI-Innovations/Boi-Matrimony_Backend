package com.matrimony.service;

import org.springframework.web.multipart.MultipartFile;

import com.matrimony.exception.GenericException;
import com.matrimony.model.entity.ResponseEntity;

public interface GalleryImageService {
	ResponseEntity uploadOrUpdateProfilePicture(MultipartFile profilePicture);

	byte[] getImageByPath(String imagePath) throws GenericException;

	ResponseEntity addImage(MultipartFile image);

	ResponseEntity deleteGallaryImage(Long imageId);

	ResponseEntity deleteProfilePicture();

	ResponseEntity getAllGalleryImages();
	
	ResponseEntity getAllGalleryImagesByProfileId(Long profileId);
}
