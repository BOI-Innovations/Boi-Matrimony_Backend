package com.matrimony.controller.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.exception.GenericException;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.GalleryImageRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.service.GalleryImageService;
import com.matrimony.service.PrivacySettingsService;
import com.matrimony.service.ProfileService;
import com.matrimony.service.UserService;

@RestController
@RequestMapping("/api/images")
public class ImageController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private GalleryImageService galleryImageService;

	@Autowired
	private GalleryImageRepository galleryImageRepository;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PrivacySettingsService privacySettingsService;
	
	
	

	@PostMapping("/uploadOrUpdateProfilePicture")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity uploadOrUpdateProfilePicture(@RequestParam MultipartFile profilePicture) {
		return galleryImageService.uploadOrUpdateProfilePicture(profilePicture);
	}

	@DeleteMapping("/deleteProfilePicture")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity deleteProfilePicture() {
		return galleryImageService.deleteProfilePicture();
	}

	@GetMapping("/getLoggedUserProfileImage")
	@PreAuthorize("hasRole('USER')")
	public org.springframework.http.ResponseEntity<ByteArrayResource> getLoggedUserProfileImage()
			throws GenericException {
		try {

			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.getUserByUsername(username);

			Optional<Profile> optionalProfile = profileRepository.findByUserId(user.getId());
			if (optionalProfile.isEmpty()) {
				String errorMessage = "Profile not found for user: " + user;
				return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND.value())
						.body(new ByteArrayResource(errorMessage.getBytes()));
			}

			Profile profile = optionalProfile.get();
			String imagePath = profile.getProfilePictureUrl();

			if (imagePath == null || imagePath.isEmpty()) {
				String errorMessage = "No profile picture set for user: " + username;
				return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND.value())
						.body(new ByteArrayResource(errorMessage.getBytes()));

			}

			byte[] imageData = galleryImageService.getImageByPath(imagePath);

			Path filePath = Paths.get(imagePath);
			String contentType = Files.probeContentType(filePath);

			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			ByteArrayResource resource = new ByteArrayResource(imageData);
			return org.springframework.http.ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.contentLength(imageData.length).body(resource);

		} catch (IOException e) {
			String errorMessage = "Error reading profile picture content type";
			return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.body(new ByteArrayResource(errorMessage.getBytes()));
		}
	}

	@GetMapping("/getUserProfileImageById/{id}")
	@PreAuthorize("hasRole('USER')")
	public org.springframework.http.ResponseEntity<ByteArrayResource> getUserProfileImageById(@PathVariable Long id)
			throws GenericException {
		try {
			Optional<Profile> optionalProfile = profileRepository.findById(id);

			if (optionalProfile.isEmpty()) {
				String errorMessage = "Profile not found for id: " + id;
				return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND.value())
						.body(new ByteArrayResource(errorMessage.getBytes()));
			}

			Profile profile = optionalProfile.get();
			String imagePath = profile.getProfilePictureUrl();

			if (imagePath == null || imagePath.isEmpty()) {
				String errorMessage = "No profile picture set for user with id: " + id;
				return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND.value())
						.body(new ByteArrayResource(errorMessage.getBytes()));
			}

			byte[] imageData = galleryImageService.getImageByPath(imagePath);

			Path filePath = Paths.get(imagePath);
			String contentType = Files.probeContentType(filePath);

			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			ByteArrayResource resource = new ByteArrayResource(imageData);
			return org.springframework.http.ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.contentLength(imageData.length).body(resource);

		} catch (IOException e) {
			String errorMessage = "Error reading profile picture content type";
			return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.body(new ByteArrayResource(errorMessage.getBytes()));
		}
	}

	@PostMapping("/upload-gallery-image")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity uploadGalleryImage(@RequestParam MultipartFile image) {
		return galleryImageService.addImage(image);
	}

	@DeleteMapping("/delete-gallery-image/{imageId}")
	public ResponseEntity deleteGallaryImage(@PathVariable Long imageId) {
		return galleryImageService.deleteGallaryImage(imageId);
	}

	@GetMapping("/getGalleryImageById")
	@PreAuthorize("hasRole('USER')")
	public org.springframework.http.ResponseEntity<ByteArrayResource> getGalleryImageById(@RequestParam String id)
			throws GenericException {
		try {
			String path = galleryImageRepository.findImageUrlById(Long.parseLong(id));
			byte[] imageData = galleryImageService.getImageByPath(path);
			Path filePath = Paths.get(path);
			String contentType = Files.probeContentType(filePath);

			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			ByteArrayResource resource = new ByteArrayResource(imageData);
			return org.springframework.http.ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.contentLength(imageData.length).body(resource);

		} catch (IOException e) {
			throw new GenericException("Error reading profile picture content type", e);
		}
	}

//	@GetMapping("/getProfileImageById")
//	@PreAuthorize("hasRole('USER')")
//	public org.springframework.http.ResponseEntity<ByteArrayResource> getProfileImageById(
//			@RequestParam String profileId) throws GenericException {
//		try {
//			Optional<Profile> profileDetails = profileRepository.findById(Long.parseLong(profileId));
//
//			if (!profileDetails.isPresent()) {
//				throw new GenericException("Profile not found for profile ID: " + profileId);
//			}
//
//			String imagePath = profileDetails.get().getProfilePictureUrl();
//
//			if (imagePath == null || imagePath.isEmpty()) {
//				throw new GenericException("Profile picture not available for profile ID: " + profileId);
//			}
//
//			byte[] imageData = galleryImageService.getImageByPath(imagePath);
//
//			Path filePath = Paths.get(imagePath);
//			if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
//				throw new GenericException("Image file does not exist or is not a valid file: " + imagePath);
//			}
//
//			String contentType = Files.probeContentType(filePath);
//			if (contentType == null) {
//				contentType = "application/octet-stream";
//			}
//
//			ByteArrayResource resource = new ByteArrayResource(imageData);
//
//			return org.springframework.http.ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
//					.contentLength(imageData.length).body(resource);
//
//		} catch (IOException e) {
//			throw new GenericException("Error reading profile image content type", e);
//		} catch (NumberFormatException e) {
//			throw new GenericException("Invalid profile ID format: " + profileId, e);
//		} catch (Exception e) {
//			throw new GenericException("An unexpected error occurred while fetching profile image", e);
//		}
//	}
	@GetMapping("/getProfileImageById")
	@PreAuthorize("hasRole('USER')")
	public org.springframework.http.ResponseEntity<ByteArrayResource> getProfileImageById(
	        @RequestParam String profileId) throws GenericException {
	    try {
	        Long userId = profileService.getCurrentUserId();
	        User viewer = userRepository.findById(userId)
	                .orElseThrow(() -> new GenericException("Viewer not found"));

	        Profile profile = profileRepository.findById(Long.parseLong(profileId))
	                .orElseThrow(() -> new GenericException("Profile not found for profile ID: " + profileId));
	        User owner = profile.getUser();
	        if (!privacySettingsService.canViewPhoto(viewer, owner)) {
	            throw new GenericException("You are not allowed to view this profile photo");
	        }

	        String imagePath = profile.getProfilePictureUrl();

	        if (imagePath == null || imagePath.isEmpty()) {
	            throw new GenericException("Profile picture not available for profile ID: " + profileId);
	        }

	        byte[] imageData = galleryImageService.getImageByPath(imagePath);

	        Path filePath = Paths.get(imagePath);

	        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
	            throw new GenericException("Image file does not exist: " + imagePath);
	        }

	        String contentType = Files.probeContentType(filePath);

	        if (contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        ByteArrayResource resource = new ByteArrayResource(imageData);

	        return org.springframework.http.ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .contentLength(imageData.length)
	                .body(resource);

	    } 
	    catch (GenericException e) {
	        throw e;
	    }
	    catch (IOException e) {
	        throw new GenericException("Error reading profile image");
	    }
	    catch (NumberFormatException e) {
	        throw new GenericException("Invalid profile ID format");
	    }
	}
	@GetMapping("/getAllGalleryImages")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getAllGalleryImages() {

		return galleryImageService.getAllGalleryImages();
	}

	@GetMapping("/getAllGalleryImagesByProfileId")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getAllGalleryImagesByProfileId(@RequestParam Long profileId) {
		return galleryImageService.getAllGalleryImagesByProfileId(profileId);
	}

}
