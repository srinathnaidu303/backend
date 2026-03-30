package com.hms.service;

import com.hms.dto.DoctorDto;
import com.hms.entity.Doctor;
import com.hms.entity.Specialty;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.DoctorRepository;
import com.hms.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;

    public DoctorService(DoctorRepository doctorRepository, SpecialtyRepository specialtyRepository) {
        this.doctorRepository = doctorRepository;
        this.specialtyRepository = specialtyRepository;
    }

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return mapToDto(doctor);
    }

    public DoctorDto createDoctor(DoctorDto dto) {
        Specialty specialty = specialtyRepository.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialty not found"));

        Doctor doctor = new Doctor(dto.getName(), specialty, dto.isAvailable());
        Doctor saved = doctorRepository.save(doctor);
        return mapToDto(saved);
    }

    public DoctorDto updateDoctor(Long id, DoctorDto dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (!doctor.getSpecialty().getId().equals(dto.getSpecialtyId())) {
            Specialty specialty = specialtyRepository.findById(dto.getSpecialtyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Specialty not found"));
            doctor.setSpecialty(specialty);
        }

        doctor.setName(dto.getName());
        doctor.setAvailable(dto.isAvailable());

        Doctor saved = doctorRepository.save(doctor);
        return mapToDto(saved);
    }

    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor not found");
        }
        doctorRepository.deleteById(id);
    }

    private DoctorDto mapToDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setSpecialtyId(doctor.getSpecialty().getId());
        dto.setSpecialtyName(doctor.getSpecialty().getName());
        dto.setAvailable(doctor.isAvailable());
        return dto;
    }
}
