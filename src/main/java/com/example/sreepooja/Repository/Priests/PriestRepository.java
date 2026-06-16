package com.example.sreepooja.Repository.Priests;

import com.example.sreepooja.Entity.Priests.Priest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriestRepository
        extends JpaRepository<Priest, Long> {

    boolean existsByMobileNumber(
            String mobileNumber
    );

    boolean existsByAadhaarNumber(
            String aadhaarNumber
    );

    Page<Priest> findByActive(
            Boolean active,
            Pageable pageable
    );
}
