package com.example.sreepooja.Repository.Masters;

import com.example.sreepooja.Entity.Masters.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    boolean existsByCityNameIgnoreCaseAndStateId(
            String cityName,
            Long stateId
    );

    List<City> findByActiveTrue();

    List<City> findByStateId(Long stateId);

    Optional<City> findByIdAndActiveTrue(Long id);
}