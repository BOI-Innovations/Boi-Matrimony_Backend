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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.exception.GenericException;
import com.matrimony.model.dto.request.SponsoredAdRequestDTO;
import com.matrimony.model.dto.response.SponsoredAdResponseDTO;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.SponsoredAd;
import com.matrimony.repository.SponsoredAdRepository;
import com.matrimony.service.SponsoredAdService;

@Service
public class SponsoredAdServiceImpl implements SponsoredAdService {

	@Autowired
	private SponsoredAdRepository sponsoredAdRepository;

	@Value("${ads.media.upload-dir}")
	private String adsMediaUploadDir;

	@Override
	public ResponseEntity createAd(SponsoredAdRequestDTO requestDTO, MultipartFile mediaFile) {

		try {
			if (mediaFile == null || mediaFile.isEmpty()) {
				return new ResponseEntity("No media file uploaded", 400, null);
			}

			File uploadDir = new File(adsMediaUploadDir);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			String fileName = UUID.randomUUID() + "_" + mediaFile.getOriginalFilename();
			Path mediaPath = Paths.get(adsMediaUploadDir, fileName);
			Files.copy(mediaFile.getInputStream(), mediaPath, StandardCopyOption.REPLACE_EXISTING);

			SponsoredAd ad = convertToEntity(requestDTO);
			ad.setMediaUrl(mediaPath.toString());
			ad.setCreatedAt(LocalDateTime.now());
			ad.setUpdatedAt(LocalDateTime.now());

			SponsoredAd savedAd = sponsoredAdRepository.save(ad);

			return new ResponseEntity("Sponsored ad created successfully", HttpStatus.OK.value(),
					convertToResponse(savedAd));

		} catch (Exception e) {
			return new ResponseEntity("Error creating ad: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAdById(Long id) {
		try {
			Optional<SponsoredAd> optionalAd = sponsoredAdRepository.findById(id);
			if (optionalAd.isEmpty()) {
				return new ResponseEntity("Ad not found", 404, null);
			}

			return new ResponseEntity("Sponsored ad fetched successfully", 200, convertToResponse(optionalAd.get()));

		} catch (Exception e) {
			return new ResponseEntity("Error fetching ad: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllActiveAds() {
		try {
			List<SponsoredAd> ads = sponsoredAdRepository.findByIsActiveTrueOrderByDisplayOrderAsc();

			return new ResponseEntity("Sponsored ads fetched successfully", 200, convertToResponseList(ads));

		} catch (Exception e) {
			return new ResponseEntity("Error fetching ads: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity updateAd(Long id, SponsoredAdRequestDTO requestDTO, MultipartFile mediaFile) {

		try {
			Optional<SponsoredAd> optionalAd = sponsoredAdRepository.findById(id);
			if (optionalAd.isEmpty()) {
				return new ResponseEntity("Ad not found", 404, null);
			}

			SponsoredAd ad = optionalAd.get();
			ad.setTitle(requestDTO.getTitle());
			ad.setDescription(requestDTO.getDescription());
			ad.setMediaType(requestDTO.getMediaType());
			ad.setCtaText(requestDTO.getCtaText());
			ad.setCtaUrl(requestDTO.getCtaUrl());
			ad.setDisplayOrder(requestDTO.getDisplayOrder());
			ad.setActive(requestDTO.isActive());

			if (mediaFile != null && !mediaFile.isEmpty()) {

				if (ad.getMediaUrl() != null) {
					Path oldPath = Paths.get(ad.getMediaUrl());
					if (Files.exists(oldPath)) {
						Files.delete(oldPath);
					}
				}

				String fileName = UUID.randomUUID() + "_" + mediaFile.getOriginalFilename();
				Path mediaPath = Paths.get(adsMediaUploadDir, fileName);
				Files.copy(mediaFile.getInputStream(), mediaPath, StandardCopyOption.REPLACE_EXISTING);
				ad.setMediaUrl(mediaPath.toString());
			}

			ad.setUpdatedAt(LocalDateTime.now());
			SponsoredAd updatedAd = sponsoredAdRepository.save(ad);

			return new ResponseEntity("Sponsored ad updated successfully", 200, convertToResponse(updatedAd));

		} catch (Exception e) {
			return new ResponseEntity("Error updating ad: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity deleteAd(Long id) {
		try {
			Optional<SponsoredAd> optionalAd = sponsoredAdRepository.findById(id);
			if (optionalAd.isEmpty()) {
				return new ResponseEntity("Ad not found", 404, null);
			}

			sponsoredAdRepository.delete(optionalAd.get());
			return new ResponseEntity("Sponsored ad deleted successfully", 200, null);

		} catch (Exception e) {
			return new ResponseEntity("Error deleting ad: " + e.getMessage(), 500, null);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] getAdMediaByPath(String mediaPath) throws GenericException {

		try {
			Path path = Paths.get(mediaPath).toAbsolutePath();
			Path baseDir = Paths.get(adsMediaUploadDir).toAbsolutePath();

			if (!path.startsWith(baseDir)) {
				throw new GenericException("Access forbidden");
			}

			if (!Files.exists(path) || !Files.isRegularFile(path)) {
				throw new GenericException("Media file not found");
			}

			return Files.readAllBytes(path);

		} catch (IOException e) {
			throw new GenericException("Failed to read media file", e);
		}
	}

	/* -------------------- MAPPERS -------------------- */

	private SponsoredAd convertToEntity(SponsoredAdRequestDTO dto) {
		SponsoredAd ad = new SponsoredAd();
		ad.setTitle(dto.getTitle());
		ad.setDescription(dto.getDescription());
		ad.setMediaType(dto.getMediaType());
		ad.setCtaText(dto.getCtaText());
		ad.setCtaUrl(dto.getCtaUrl());
		ad.setDisplayOrder(dto.getDisplayOrder());
		ad.setActive(dto.isActive());
		return ad;
	}

	private SponsoredAdResponseDTO convertToResponse(SponsoredAd ad) {
		SponsoredAdResponseDTO dto = new SponsoredAdResponseDTO();
		dto.setId(ad.getId());
		dto.setTitle(ad.getTitle());
		dto.setDescription(ad.getDescription());
		dto.setMediaType(ad.getMediaType());
		dto.setMediaUrl(ad.getMediaUrl());
		dto.setCtaText(ad.getCtaText());
		dto.setCtaUrl(ad.getCtaUrl());
		dto.setActive(ad.isActive());
		dto.setDisplayOrder(ad.getDisplayOrder());
		return dto;
	}

	private List<SponsoredAdResponseDTO> convertToResponseList(List<SponsoredAd> ads) {
		return ads.stream().map(this::convertToResponse).collect(Collectors.toList());
	}
}
