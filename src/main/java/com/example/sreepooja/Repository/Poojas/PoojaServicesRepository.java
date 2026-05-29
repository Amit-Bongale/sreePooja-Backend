package com.example.sreepooja.Repository.Poojas;

import com.example.sreepooja.Entity.Poojas.PoojaServices;
import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PoojaServicesRepository
        extends JpaRepository<PoojaServices, Long> {

    Optional<PoojaServices> findById(Long id);

    // PUBLIC USER SIDE
    Optional<PoojaServices> findBySlugAndStatus(
            String slug,
            ServiceStatus status
    );

    // ADMIN SIDE
    Optional<PoojaServices> findBySlug(String slug);

    List<PoojaServices> findAll();

    List<PoojaServices> findByStatus(ServiceStatus status);

    boolean existsBySlug(String slug);


    // FILTER SERVICES

    @Query("""
            SELECT DISTINCT ps
            FROM PoojaServices ps
            LEFT JOIN ps.locations loc
            LEFT JOIN ps.languages lang
            LEFT JOIN ps.communities comm

            WHERE
            (:categorySlug IS NULL
                OR ps.category.slug = :categorySlug)

            AND
            (:cityId IS NULL
                OR loc.city.id = :cityId)

            AND
            (:languageId IS NULL
                OR lang.language.id = :languageId)

            AND
            (:communityId IS NULL
                OR comm.community.id = :communityId)

            AND
            (:search IS NULL
                OR LOWER(ps.serviceName)
                   LIKE LOWER(CONCAT('%', :search, '%')))

            AND ps.status =
                com.example.sreepooja.Enum.Poojas.ServiceStatus.ACTIVE
            """)
    List<PoojaServices> filterServices(

            @Param("categorySlug")
            String categorySlug,

            @Param("cityId")
            Long cityId,

            @Param("languageId")
            Long languageId,

            @Param("communityId")
            Long communityId,

            @Param("search")
            String search
    );

    List<PoojaServices> findByFeaturedTrueAndStatus(
            ServiceStatus status
    );
}