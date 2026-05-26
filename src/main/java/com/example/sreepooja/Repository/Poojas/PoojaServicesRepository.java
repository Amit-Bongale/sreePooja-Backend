package com.example.sreepooja.Repository.Poojas;

import com.example.sreepooja.Entity.Poojas.PoojaServices;
import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PoojaServicesRepository extends JpaRepository<PoojaServices, Long> {

    Optional<PoojaServices> findByIdAndDeletedFalse(Long id);

    Optional<PoojaServices> findBySlugAndDeletedFalse(String slug);

    List<PoojaServices> findByDeletedFalse();

    List<PoojaServices> findByStatusAndDeletedFalse(ServiceStatus status);

    boolean existsBySlug(String slug);


    // FILTER BY CITY

    @Query("""
            SELECT DISTINCT ps
            FROM PoojaServices ps
            JOIN ps.locations loc
            WHERE loc.city.id = :cityId
            AND ps.deleted = false
            """)
    List<PoojaServices> findByCity(
            @Param("cityId") Long cityId
    );


    // FILTER BY LANGUAGE

    @Query("""
            SELECT DISTINCT ps
            FROM PoojaServices ps
            JOIN ps.languages lang
            WHERE lang.language.id = :languageId
            AND ps.deleted = false
            """)
    List<PoojaServices> findByLanguage(
            @Param("languageId") Long languageId
    );


    // FILTER BY COMMUNITY

    @Query("""
            SELECT DISTINCT ps
            FROM PoojaServices ps
            JOIN ps.communities comm
            WHERE comm.community.id = :communityId
            AND ps.deleted = false
            """)
    List<PoojaServices> findByCommunity(
            @Param("communityId") Long communityId
    );


    // FILTER BY CITY + LANGUAGE + COMMUNITY

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

AND ps.deleted = false
""")
    List<PoojaServices> filterServices(
            @Param("categorySlug") String categorySlug,

            @Param("cityId") Long cityId,

            @Param("languageId") Long languageId,

            @Param("communityId") Long communityId,

            @Param("search") String search
    );

}