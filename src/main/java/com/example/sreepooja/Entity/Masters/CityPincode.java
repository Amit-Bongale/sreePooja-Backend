package com.example.sreepooja.Entity.Masters;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "city_pincodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityPincode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String pincode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Builder.Default
    private Boolean active = true;
}