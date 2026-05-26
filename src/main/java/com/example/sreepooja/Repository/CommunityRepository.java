package com.example.sreepooja.Repository;

import com.example.sreepooja.Entity.Masters.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}