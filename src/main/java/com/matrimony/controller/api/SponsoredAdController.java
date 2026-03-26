package com.matrimony.controller.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrimony.exception.GenericException;
import com.matrimony.model.dto.request.SponsoredAdRequestDTO;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.SponsoredAd;
import com.matrimony.repository.SponsoredAdRepository;
import com.matrimony.service.SponsoredAdService;

@RestController
@RequestMapping("/api/sponsored-ads")
public class SponsoredAdController {

	@Autowired
	private SponsoredAdService sponsoredAdService;

	@Autowired
	private SponsoredAdRepository sponsoredAdRepository;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@PostMapping("/create")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity createAd(@RequestParam("ad") String adJson, @RequestParam("media") MultipartFile mediaFile)
			throws IOException {
		SponsoredAdRequestDTO requestDTO = objectMapper.readValue(adJson, SponsoredAdRequestDTO.class);
		return sponsoredAdService.createAd(requestDTO, mediaFile);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity getAdById(@PathVariable Long id) {
		return sponsoredAdService.getAdById(id);
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity getAllActiveAds() {
		return sponsoredAdService.getAllActiveAds();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity updateAd(@PathVariable Long id, @RequestParam("ad") String adJson,
			@RequestParam(value = "media", required = false) MultipartFile mediaFile) throws IOException {

		SponsoredAdRequestDTO requestDTO = objectMapper.readValue(adJson, SponsoredAdRequestDTO.class);

		return sponsoredAdService.updateAd(id, requestDTO, mediaFile);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity deleteAd(@PathVariable Long id) {
		return sponsoredAdService.deleteAd(id);
	}

	@GetMapping("/media/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public org.springframework.http.ResponseEntity<ByteArrayResource> getAdMediaById(@PathVariable Long id)
			throws GenericException {

		try {
			Optional<SponsoredAd> optionalAd = sponsoredAdRepository.findById(id);

			if (optionalAd.isEmpty()) {
				return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ByteArrayResource("Ad not found".getBytes()));
			}

			SponsoredAd ad = optionalAd.get();
			String mediaPath = ad.getMediaUrl();

			if (mediaPath == null || mediaPath.isBlank()) {
				return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ByteArrayResource("No media found".getBytes()));
			}

			byte[] mediaData = sponsoredAdService.getAdMediaByPath(mediaPath);

			Path filePath = Path.of(mediaPath);
			String contentType = Files.probeContentType(filePath);

			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			return org.springframework.http.ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.contentLength(mediaData.length).body(new ByteArrayResource(mediaData));

		} catch (IOException e) {
			return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ByteArrayResource("Failed to read media".getBytes()));
		}
	}
}