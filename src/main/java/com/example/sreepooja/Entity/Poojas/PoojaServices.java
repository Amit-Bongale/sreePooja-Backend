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

    @Column(unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ServiceCategory category;

    @Column(length = 500)
    private String shortDescription;

    @Lob
    private String fullDescription;

    @Lob
    private String benefits;

    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @Builder.Default
    private Boolean featured = false;

    @Builder.Default
    private Boolean cancellationAllowed = true;

    @Builder.Default
    private Boolean refundAllowed = true;

    private String metaTitle;

    @Column(length = 1000)
    private String metaDescription;

    private String metaKeywords;

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

    @OneToMany(
            mappedBy = "poojaService",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ServiceLanguageMapping> languages = new ArrayList<>();

    @OneToMany(
            mappedBy = "poojaService",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ServiceCommunityMapping> communities = new ArrayList<>();

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

    @Builder.Default
    @Column(nullable = false)
    private Boolean deleted = false;
}

