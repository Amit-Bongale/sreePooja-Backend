package com.example.sreepooja.Repository.Poojas;

import com.example.sreepooja.Entity.Poojas.ServiceCategory;
import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    List<ServiceCategory> findAll();

    Optional<ServiceCategory> findById(Long Id);

    Optional<ServiceCategory> findBySlug(String categorySlug);

    boolean existsByCategoryName(String categoryName);

    boolean existsBySlug(String slug);

    List<ServiceCategory> findByStatus(ServiceStatus status);
}