package com.example.sreepooja.Repository.Masters;

import com.example.sreepooja.Entity.Masters.CityPincode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityPincodeRepository
        extends JpaRepository<CityPincode, Long> {

    boolean existsByPincode(String pincode);

    List<CityPincode> findByCityIdAndActiveTrue(Long cityId);

    Optional<CityPincode> findByIdAndActiveTrue(Long cityId);

    List<CityPincode> findByCityId(Long cityId);
}