package com.example.sreepooja.Repository.Priests;

import com.example.sreepooja.Entity.Priests.Priest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriestRepository
        extends JpaRepository<Priest, Long>,
        JpaSpecificationExecutor<Priest> {

    @Override
    @EntityGraph(
            attributePaths = {
                    "user",
                    "community"
            }
    )
    Page<Priest> findAll(
            Specification<Priest> spec,
            Pageable pageable
    );

    @EntityGraph(
            attributePaths = {
                    "user",
                    "community"
            }
    )
    Optional<Priest> findByUserId(
            Long userId
    );

    @Override
    @EntityGraph(
            attributePaths = {
                    "user",
                    "community"
            }
    )
    Optional<Priest> findById(
            Long id
    );

    boolean existsByAadhaarNumber(
            String aadhaarNumber
    );

    boolean existsByWhatsappNumber(
            String whatsappNumber
    );

    boolean existsByWhatsappNumberAndIdNot(
            String whatsappNumber,
            Long id
    );
}
