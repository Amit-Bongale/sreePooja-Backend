package com.example.sreepooja.Entity.Poojas;

import com.example.sreepooja.Enum.Poojas.PackageType;
import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "service_packages",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"service_id", "packageType"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageType packageType;

    private String shortDescription;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String includedItems;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 5, scale = 2)
    private BigDecimal advancePercentage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private PoojaServices poojaService;
}