package com.hms.config;

import com.hms.entity.Doctor;
import com.hms.entity.Specialty;
import com.hms.repository.DoctorRepository;
import com.hms.repository.SpecialtyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SpecialtyRepository specialtyRepository;
    private final DoctorRepository doctorRepository;

    public DataSeeder(SpecialtyRepository specialtyRepository, DoctorRepository doctorRepository) {
        this.specialtyRepository = specialtyRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void run(String... args) {
        // Step 1: Seed Specialties
        if (specialtyRepository.count() == 0) {
            List<Specialty> specs = List.of(
                    new Specialty("Cardiology"),
                    new Specialty("Neurology"),
                    new Specialty("Orthopedics"),
                    new Specialty("Dermatology"),
                    new Specialty("Pediatrics")
            );
            specialtyRepository.saveAll(specs);
        }

        // Fetch specialties to associate with doctors
        Map<String, Specialty> specialtyMap = new HashMap<>();
        specialtyRepository.findAll().forEach(s -> specialtyMap.put(s.getName(), s));

        // Step 2: Seed Doctors
        if (doctorRepository.count() == 0) {
            doctorRepository.saveAll(List.of(
                    new Doctor("John Smith", specialtyMap.get("Cardiology"), true),
                    new Doctor("Emily Johnson", specialtyMap.get("Neurology"), true),
                    new Doctor("Michael Brown", specialtyMap.get("Orthopedics"), true),
                    new Doctor("Sarah Davis", specialtyMap.get("Dermatology"), true),
                    new Doctor("David Wilson", specialtyMap.get("Pediatrics"), true)
            ));
        }
    }
}