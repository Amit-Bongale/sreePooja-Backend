package com.example.sreepooja.Repository.Poojas;

import com.example.sreepooja.Entity.Poojas.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {

    List<ServicePackage> findByPoojaServiceId(Long serviceId);
}