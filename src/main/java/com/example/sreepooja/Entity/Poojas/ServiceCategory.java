package com.example.sreepooja.Entity.Poojas;

import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "service_categories",
        indexes = {
                @Index(name = "idx_category_name", columnList = "categoryName"),
                @Index(name = "idx_category_slug", columnList = "slug")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String categoryName;

    @Column(unique = true, nullable = false)
    private String slug;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;
}