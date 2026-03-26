package com.matrimony.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.exception.GenericException;
import com.matrimony.model.dto.response.GalleryImageResponse;
import com.matrimony.model.entity.GalleryImage;
import com.matrimony.model.entity.Profile;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.GalleryImageRepository;
import com.matrimony.repository.ProfileRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.service.GalleryImageService;
import com.matrimony.service.ProfileService;
import com.matrimony.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class GalleryImageServiceImpl implements GalleryImageService {

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private GalleryImageRepository galleryImageRepository;

	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ProfileService profileService;

	@Value("${profile.picture.upload-dir}")
	private String PROFILE_PICTURE_UPLOAD_DIR;

	@Transactional
	@Override
	public ResponseEntity uploadOrUpdateProfilePicture(MultipartFile profilePicture) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.getUserByUsername(username);

			Optional<Profile> optionalProfile = profileRepository.findByUserId(user.getId());
			if (optionalProfile.isEmpty()) {
				return new ResponseEntity("Profile not found for user: " + username, 404, null);
			}

			Profile profile = optionalProfile.get();

			if (profilePicture == null || profilePicture.isEmpty()) {
				return new ResponseEntity("No file uploaded", 400, null);
			}

			String userDirPath = Paths.get(PROFILE_PICTURE_UPLOAD_DIR, username + "_" + String.valueOf(user.getId()))
					.toString();
			File userDir = new File(userDirPath);
			if (!userDir.exists()) {
				userDir.mkdirs();
			}

			if (profile.getProfilePictureUrl() != null) {
				File oldFile = new File(profile.getProfilePictureUrl());
				if (oldFile.exists()) {
					oldFile.delete();
				}
			}

			String fileName = UUID.randomUUID() + "_" + profilePicture.getOriginalFilename();
			Path filePath = Paths.get(userDirPath, fileName);
			Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			profile.setProfilePictureUrl(filePath.toString());
			profile.setUpdatedAt(LocalDateTime.now());

			Profile updatedProfile = profileRepository.save(profile);
			profileService.calculateProfileCompletion(updatedProfile.getId());
			return new ResponseEntity("Profile picture uploaded successfully", 200,
					updatedProfile.getProfilePictureUrl());

		} catch (Exception e) {
			return new ResponseEntity("Error uploading profile picture: " + e.getMessage(), 500, null);
		}
	}

	@Transactional
	@Override
	public ResponseEntity addImage(MultipartFile image) {
		try {

			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = userRepository.findUserIdByUsername(username);
			
			if (userId == null) {
				return new ResponseEntity("User not found", 404, null);
			}

			Long profileId = profileRepository.findProfileIdByUserId(userId);
			if (profileId == null) {
				return new ResponseEntity("Profile not found", 404, null);
			}

			if (image == null || image.isEmpty()) {
				return new ResponseEntity("No image uploaded", 400, null);
			}

			String userDirPath = Paths.get(PROFILE_PICTURE_UPLOAD_DIR, username + "_" + userId, "gallery").toString();
			File userDir = new File(userDirPath);
			if (!userDir.exists()) {
				userDir.mkdirs();
			}

			String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
			Path filePath = Paths.get(userDirPath, fileName);
			Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			Profile profileRef = entityManager.getReference(Profile.class, profileId);

			GalleryImage galleryImage = new GalleryImage();
			galleryImage.setProfile(profileRef);
			galleryImage.setImageUrl(filePath.toString());
			long imageSizeInBytes = image.getSize();
			long imageSizeInKB = imageSizeInBytes / 1024;
			galleryImage.setImageSize(imageSizeInKB);
			galleryImage.setImageFormat(getFileExtension(image.getOriginalFilename()));
			galleryImageRepository.save(galleryImage);
			profileService.calculateProfileCompletion(profileId);
			return new ResponseEntity("Image uploaded successfully", 200, galleryImage.getImageUrl());

		} catch (Exception e) {
			return new ResponseEntity("Error uploading image: " + e.getMessage(), 500, null);
		}
	}

	private String getFileExtension(String filename) {
		int lastIndexOfDot = filename.lastIndexOf('.');
		if (lastIndexOfDot != -1 && lastIndexOfDot < filename.length() - 1) {
			return filename.substring(lastIndexOfDot + 1).toLowerCase();
		}
		return "";
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] getImageByPath(String imagePath) throws GenericException {
		try {
			Path path = Paths.get(imagePath).toAbsolutePath();

			Path uploadBaseDir = Paths.get(PROFILE_PICTURE_UPLOAD_DIR).toAbsolutePath();
			if (!path.startsWith(uploadBaseDir)) {
				throw new GenericException("Access to this path is forbidden");
			}

			if (!Files.exists(path) || !Files.isRegularFile(path)) {
				throw new GenericException("Profile picture does not exist or is not a regular file: " + imagePath);
			}

			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new GenericException("Failed to read profile picture from path: " + imagePath, e);
		}
	}

	@Transactional
	@Override
	public ResponseEntity deleteGallaryImage(Long imageId) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.getUserByUsername(username);

			Optional<Profile> optionalProfile = profileRepository.findByUserId(user.getId());
			if (optionalProfile.isEmpty()) {
				return new ResponseEntity("Profile not found for user: " + username, 404, null);
			}

			Profile profile = optionalProfile.get();

			Optional<GalleryImage> optionalImage = galleryImageRepository.findByIdAndProfileId(imageId,
					profile.getId());
			if (optionalImage.isEmpty()) {
				return new ResponseEntity("Image not found or does not belong to your profile", 404, null);
			}

			GalleryImage imageToDelete = optionalImage.get();

			// Delete file from disk
			File file = new File(imageToDelete.getImageUrl());
			if (file.exists()) {
				file.delete();
			}

			// Delete image entity
			galleryImageRepository.delete(imageToDelete);

			profile.setUpdatedAt(LocalDateTime.now());
			profileRepository.save(profile);

			return new ResponseEntity("Image deleted successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting image: " + e.getMessage(), 500, null);
		}
	}

	@Transactional
	@Override
	public ResponseEntity deleteProfilePicture() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.getUserByUsername(username);

			Optional<Profile> optionalProfile = profileRepository.findByUserId(user.getId());
			if (optionalProfile.isEmpty()) {
				return new ResponseEntity("Profile not found for user: " + username, 404, null);
			}

			Profile profile = optionalProfile.get();

			if (profile.getProfilePictureUrl() == null || profile.getProfilePictureUrl().isEmpty()) {
				return new ResponseEntity("No profile picture to delete", 400, null);
			}

			File fileToDelete = new File(profile.getProfilePictureUrl());
			if (fileToDelete.exists() && fileToDelete.delete()) {
				profile.setProfilePictureUrl(null);
				profile.setUpdatedAt(LocalDateTime.now());
				profileRepository.save(profile);

				return new ResponseEntity("Profile picture deleted successfully", 200, null);
			} else {
				return new ResponseEntity("Error deleting the file from the server", 500, null);
			}

		} catch (Exception e) {
			return new ResponseEntity("Error deleting profile picture: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllGalleryImages() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			List<GalleryImage> profileImages = galleryImageRepository.getProfileImageByProfileId(
					profileRepository.findProfileIdByUserId(userService.getUserByUsername(username).getId()));

			if (profileImages == null || profileImages.isEmpty()) {
				return new ResponseEntity("No gallery images found for this profile", "404", null);
			}

			List<GalleryImageResponse> responseList = profileImages.stream().map(image -> {
				GalleryImageResponse response = new GalleryImageResponse();
				response.setId(image.getId());
				response.setImageUrl(image.getImageUrl());
				response.setUploadedAt(image.getUploadedAt());
				response.setProfileId(image.getProfile().getId().toString());
				response.setImageFormat(image.getImageFormat());
				response.setImageSize(image.getImageSize() + " " + "KB");

				return response;
			}).collect(Collectors.toList());

			return new ResponseEntity("Gallery images fetched successfully", "200", responseList);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return new ResponseEntity("Error occurred while fetching gallery images", "500", e.getMessage());
		}
	}

	@Override
	public ResponseEntity getAllGalleryImagesByProfileId(Long profileId) {
		if (profileId == null) {
			return new ResponseEntity("Profile id cannot be null", HttpStatus.BAD_REQUEST.value(), null);
		} else {
			try {
				List<GalleryImage> profileImage = galleryImageRepository.findByProfileId(profileId);
				if (profileImage == null || profileImage.isEmpty()) {
					return new ResponseEntity("No gallery images found for this profile id" + profileId,
							HttpStatus.NOT_FOUND.value(), null);
				}
				List<GalleryImageResponse> responseList = profileImage.stream().map(image -> {
					GalleryImageResponse response = new GalleryImageResponse();
					response.setId(image.getId());
					response.setImageUrl(image.getImageUrl());
					response.setUploadedAt(image.getUploadedAt());
					response.setProfileId(image.getProfile().getId().toString());
					response.setImageFormat(image.getImageFormat());
					response.setImageSize(image.getImageSize() + " " + "KB");
					return response;
				}).collect(Collectors.toList());

				return new ResponseEntity("Gallery image getched successfully", HttpStatus.OK.value(), responseList);
			} catch (Exception e) {
				e.getStackTrace();
				return new ResponseEntity("Error occuring in fetching gallery images using id " + profileId,
						HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
			}
		}
	}

}
