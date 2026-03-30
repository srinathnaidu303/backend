package com.hms.controller;

import com.hms.dto.SpecialtyDto;
import com.hms.service.SpecialtyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specialties")
@CrossOrigin("*")   // 🔥 for frontend later
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public List<SpecialtyDto> getAllSpecialties() {
        return specialtyService.getAllSpecialties();
    }

    @PostMapping
    public SpecialtyDto createSpecialty(@RequestBody SpecialtyDto dto) {
        return specialtyService.createSpecialty(dto);
    }
}