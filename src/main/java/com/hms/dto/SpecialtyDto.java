package com.hms.dto;

import jakarta.validation.constraints.NotBlank;

public class SpecialtyDto {
    private Long id;

    @NotBlank(message = "Specialty name is required")
    private String name;

    public SpecialtyDto() {
    }

    public SpecialtyDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
