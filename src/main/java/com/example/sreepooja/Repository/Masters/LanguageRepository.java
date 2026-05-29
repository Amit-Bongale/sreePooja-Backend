package com.example.sreepooja.Repository.Masters;

import com.example.sreepooja.Entity.Masters.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository
        extends JpaRepository<Language, Long> {

    boolean existsByLanguageNameIgnoreCase(
            String languageName
    );

    List<Language> findByActiveTrue();

    Optional<Language> findByIdAndActiveTrue(Long id);
}