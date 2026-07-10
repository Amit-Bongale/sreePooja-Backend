package com.example.sreepooja.Entity.Priests;

import com.example.sreepooja.Entity.Masters.Language;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "priest_registration_languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriestRegistrationLanguageMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "registration_id",
            nullable = false
    )
    private PriestRegistration registration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "language_id",
            nullable = false
    )
    private Language language;
}