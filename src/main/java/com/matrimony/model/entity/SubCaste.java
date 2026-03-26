package com.matrimony.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sub_castes")
public class SubCaste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String subCasteName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubCasteName() {
        return subCasteName;
    }

    public void setSubCasteName(String subCasteName) {
        this.subCasteName = subCasteName;
    }
}
