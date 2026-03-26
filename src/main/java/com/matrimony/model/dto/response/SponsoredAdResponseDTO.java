package com.matrimony.model.dto.response;

import java.time.LocalDateTime;

import com.matrimony.model.enums.BannerType;
import com.matrimony.model.enums.MediaType;

public class SponsoredAdResponseDTO {

    private Long id;
    private String title;
    private String description;
    private MediaType mediaType;
    private String mediaUrl;
    private String ctaText;
    private String ctaUrl;
    private boolean isActive;
    private Integer displayOrder;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public MediaType getMediaType() {
		return mediaType;
	}
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public String getCtaText() {
		return ctaText;
	}
	public void setCtaText(String ctaText) {
		this.ctaText = ctaText;
	}
	public String getCtaUrl() {
		return ctaUrl;
	}
	public void setCtaUrl(String ctaUrl) {
		this.ctaUrl = ctaUrl;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
    
}
