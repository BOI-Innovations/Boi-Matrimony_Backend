package com.matrimony.model.dto.response;

import java.util.List;

public class SearchResponse {
    private List<ProfileResponse> profiles;
    private Long totalCount;
    private Integer currentPage;
    private Integer totalPages;
    private Integer pageSize;
    private Boolean hasNext;
    private Boolean hasPrevious;

    // Getters and Setters
    public List<ProfileResponse> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ProfileResponse> profiles) {
        this.profiles = profiles;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}