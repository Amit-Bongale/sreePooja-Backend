package com.example.sreepooja.Repository;

import com.example.sreepooja.Entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}