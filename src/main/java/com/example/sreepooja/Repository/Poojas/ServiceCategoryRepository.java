package com.example.sreepooja.Repository.Poojas;

import com.example.sreepooja.Entity.Poojas.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    Optional<ServiceCategory> findByIdAndDeletedFalse(Long id);

    List<ServiceCategory> findByDeletedFalse();

    boolean existsByCategoryName(String categoryName);

    boolean existsBySlug(String slug);
}