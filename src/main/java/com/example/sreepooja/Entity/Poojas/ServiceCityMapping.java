package com.example.sreepooja.Entity.Poojas;

import com.example.sreepooja.Entity.Masters.City;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "service_city_mappings",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"service_id", "city_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_service_city",
                        columnList = "service_id, city_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCityMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private PoojaServices poojaService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}