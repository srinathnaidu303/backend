package com.hms.service;

import com.hms.dto.SpecialtyDto;
import com.hms.entity.Specialty;
import com.hms.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public List<SpecialtyDto> getAllSpecialties() {
        return specialtyRepository.findAll()
                .stream()
                .map(s -> new SpecialtyDto(s.getId(), s.getName()))
                .collect(Collectors.toList());
    }

    public SpecialtyDto createSpecialty(SpecialtyDto dto) {
        Specialty specialty = new Specialty(dto.getName());
        Specialty saved = specialtyRepository.save(specialty);
        return new SpecialtyDto(saved.getId(), saved.getName());
    }
}