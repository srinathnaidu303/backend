package com.hms.service;

import com.hms.dto.AuthResponse;
import com.hms.dto.LoginRequest;
import com.hms.dto.RegisterRequest;
import com.hms.entity.Role;
import com.hms.entity.User;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public UserService(UserRepository userRepository, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // In a real app, hash password!
        user.setRole(Role.PATIENT); // Default role

        User savedUser = userRepository.save(user);

        return new AuthResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole(),
                null, "Registration successful");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        Long doctorId = null;
        if (user.getRole() == Role.DOCTOR) {
            doctorId = doctorRepository.findByUserId(user.getId())
                    .map(d -> d.getId())
                    .orElse(null);
        }

        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(),
                doctorId, "Login successful");
    }
}
