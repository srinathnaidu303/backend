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
    private final UserRepository userRepository;

    public DataSeeder(SpecialtyRepository specialtyRepository, DoctorRepository doctorRepository, UserRepository userRepository) {
        this.specialtyRepository = specialtyRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Step 1: Seed Specialties
        if (specialtyRepository.count() == 0) {
            specialtyRepository.saveAll(List.of(
                    new Specialty("Cardiology"),
                    new Specialty("Neurology"),
                    new Specialty("Orthopedics"),
                    new Specialty("Dermatology"),
                    new Specialty("Pediatrics")
            ));
        }

        // Fetch specialties
        Map<String, Specialty> specialtyMap = new HashMap<>();
        specialtyRepository.findAll().forEach(s -> specialtyMap.put(s.getName(), s));

        // Step 2: Seed Doctors and Users
        if (doctorRepository.count() == 0) {
            // Create a test doctor user
            User docUser = new User("John Smith", "doctor@hms.com", "password123", Role.DOCTOR);
            if (!userRepository.existsByEmail(docUser.getEmail())) {
                userRepository.save(docUser);
            } else {
                docUser = userRepository.findByEmail(docUser.getEmail()).get();
            }

            Doctor d1 = new Doctor("John Smith", specialtyMap.get("Cardiology"), true);
            d1.setUser(docUser);

            doctorRepository.saveAll(List.of(
                    d1,
                    new Doctor("Emily Johnson", specialtyMap.get("Neurology"), true),
                    new Doctor("Michael Brown", specialtyMap.get("Orthopedics"), true),
                    new Doctor("Sarah Davis", specialtyMap.get("Dermatology"), true),
                    new Doctor("David Wilson", specialtyMap.get("Pediatrics"), true)
            ));
        }
    }
}