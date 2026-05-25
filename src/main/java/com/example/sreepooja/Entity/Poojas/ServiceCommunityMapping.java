package com.example.sreepooja.Entity.Poojas;

import com.example.sreepooja.Entity.Community;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "service_community_mappings",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"service_id", "community_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_service_community",
                        columnList = "service_id, community_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCommunityMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private PoojaServices poojaService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;
}