package com.example.sreepooja.Repository.Masters;

import com.example.sreepooja.Entity.Masters.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository
        extends JpaRepository<Community, Long> {

    boolean existsByCommunityNameIgnoreCase(
            String communityName
    );

    List<Community> findByActiveTrue();

    Optional<Community> findByIdAndActiveTrue(Long id);
}