package com.example.sreepooja.Repository.Priests;

import com.example.sreepooja.Entity.Priests.Priest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriestRepository
        extends JpaRepository<Priest, Long>,
        JpaSpecificationExecutor<Priest> {

    boolean existsByMobileNumberOrWhatsappNumberOrMobileNumberOrWhatsappNumber(
            String mobile1,
            String whatsapp1,
            String mobile2,
            String whatsapp2
    );

    @Query("""
       SELECT COUNT(p) > 0
       FROM Priest p
       WHERE p.id <> :priestId
       AND (
            p.mobileNumber = :mobile
            OR p.whatsappNumber = :mobile
            OR p.mobileNumber = :whatsapp
            OR p.whatsappNumber = :whatsapp
       )
       """)
    boolean existsDuplicateNumbers(
            @Param("priestId") Long priestId,
            @Param("mobile") String mobile,
            @Param("whatsapp") String whatsapp
    );

    @Query("""
       SELECT COUNT(p) > 0
       FROM Priest p
       WHERE p.mobileNumber = :mobile
          OR p.whatsappNumber = :mobile
          OR p.mobileNumber = :whatsapp
          OR p.whatsappNumber = :whatsapp
       """)
    boolean existsDuplicateNumbers(
            @Param("mobile") String mobile,
            @Param("whatsapp") String whatsapp
    );

    boolean existsByAadhaarNumber(
            String aadhaarNumber
    );

    Page<Priest> findByActive(
            Boolean active,
            Pageable pageable
    );
}
