package com.hms.service;

import com.hms.dto.AppointmentRequest;
import com.hms.dto.AppointmentResponse;
import com.hms.entity.Appointment;
import com.hms.entity.AppointmentStatus;
import com.hms.entity.Doctor;
import com.hms.entity.User;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository,
            DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (!doctor.isAvailable()) {
            throw new RuntimeException("Doctor is not available");
        }

        Appointment appointment = new Appointment(user, doctor, request.getAppointmentDate(), AppointmentStatus.BOOKED);
        Appointment saved = appointmentRepository.save(appointment);

        return mapToDto(saved);
    }

    public List<AppointmentResponse> getUserAppointments(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return appointmentRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getDoctorAppointments(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found");
        }
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found");
        }
        appointmentRepository.deleteById(id);
    }

    private AppointmentResponse mapToDto(Appointment appointment) {
        AppointmentResponse res = new AppointmentResponse();
        res.setId(appointment.getId());
        res.setUserId(appointment.getUser().getId());
        res.setUserName(appointment.getUser().getName());
        res.setDoctorId(appointment.getDoctor().getId());
        res.setDoctorName(appointment.getDoctor().getName());
        res.setSpecialtyName(appointment.getDoctor().getSpecialty().getName());
        res.setAppointmentDate(appointment.getAppointmentDate());
        res.setStatus(appointment.getStatus());
        return res;
    }
}
