package com.hms.config;

import com.hms.entity.Specialty;
import com.hms.repository.SpecialtyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SpecialtyRepository specialtyRepository;

    public DataSeeder(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public void run(String... args) {
        if (specialtyRepository.count() == 0) {
            specialtyRepository.saveAll(List.of(
                    new Specialty("Cardiology"),
                    new Specialty("Neurology"),
                    new Specialty("Orthopedics"),
                    new Specialty("Dermatology"),
                    new Specialty("Pediatrics")
            ));
        }
    }
}