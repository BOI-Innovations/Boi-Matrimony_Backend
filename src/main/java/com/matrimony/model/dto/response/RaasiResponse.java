package com.matrimony.model.dto.response;

public class RaasiResponse {

    private Long id;
    private String name;

    public RaasiResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
