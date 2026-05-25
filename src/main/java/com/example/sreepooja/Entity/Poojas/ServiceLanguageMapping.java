package com.example.sreepooja.Entity.Poojas;

import com.example.sreepooja.Entity.Language;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "service_language_mappings",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"service_id", "language_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_service_language",
                        columnList = "service_id, language_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceLanguageMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private PoojaServices poojaService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;
}