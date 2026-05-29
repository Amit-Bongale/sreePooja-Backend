package com.example.sreepooja.Repository.Masters;

import com.example.sreepooja.Entity.Masters.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StateRepository extends JpaRepository<State, Long> {

    boolean existsByStateNameIgnoreCase(String stateName);

    List<State> findByActiveTrue();

    Optional<State> findByIdAndActiveTrue(Long id);
}