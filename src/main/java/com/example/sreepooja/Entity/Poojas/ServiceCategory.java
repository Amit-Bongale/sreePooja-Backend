package com.example.sreepooja.Entity.Poojas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, unique = true)
    private String categoryName;

    @Column(unique = true)
    private String slug;

    private Boolean active = true;

    @JsonIgnore
    @Builder.Default
    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.LAZY
    )
    private List<PoojaServices> services = new ArrayList<>();

    @Column(nullable = false)
    private Boolean deleted = false;
}