package com.example.sreepooja.Entity.Poojas;

import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "pooja_services",
        indexes = {
                @Index(name = "idx_service_slug", columnList = "slug"),
                @Index(name = "idx_service_name", columnList = "serviceName")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoojaServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serviceName;

    @Column(unique = true, nullable = false)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category;

    @Column(length = 500, nullable = false)
    private String shortDescription;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String fullDescription;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String benefits;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;

    @Column(nullable = false)
    @Builder.Default
    private Boolean featured = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean cancellationAllowed = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean refundAllowed = true;

    private String metaTitle;

    @Column(length = 1000)
    private String metaDescription;

    private String metaKeywords;

    @Column(nullable = false)
    private String thumbnailImage;

    private String bannerImage;

    @JsonIgnore
    @Builder.Default
    @OneToMany(
            mappedBy = "poojaService",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<ServicePackage> packages = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "poojaService",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ServiceLanguageMapping> languages = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "poojaService",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ServiceCommunityMapping> communities = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "poojaService",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ServiceCityMapping> locations = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

