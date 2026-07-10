package com.example.sreepooja.Repository.Priests;

import com.example.sreepooja.Entity.Priests.PriestRegistration;
import com.example.sreepooja.Enum.Priests.PriestRegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriestRegistrationRepository
        extends JpaRepository<PriestRegistration, Long> {

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByAadhaarNumber(String aadhaarNumber);

    boolean existsByWhatsappNumber(String whatsappNumber);

    Optional<PriestRegistration> findByIdAndStatus(
            Long id,
            PriestRegistrationStatus status
    );
}