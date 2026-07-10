package com.example.sreepooja.Entity.Priests;

import com.example.sreepooja.Entity.Masters.City;
import com.example.sreepooja.Entity.Masters.CityPincode;
import com.example.sreepooja.Entity.Masters.Community;
import com.example.sreepooja.Entity.Masters.State;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.Priests.PriestExperience;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "priests",
        indexes = {
                @Index(
                        name = "idx_priest_aadhaar",
                        columnList = "aadhaarNumber"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Priest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private Users user;

    @Column(nullable = false, length = 100)
    private String gothra;

    @Column(length = 200)
    private String pravara;

    @Column(nullable = false, length = 150)
    private String nativePlace;

    @Column(nullable = false, unique = true, length = 20)
    private String aadhaarNumber;

    @Column(length = 15)
    private String whatsappNumber;

    @Column(nullable = false, length = 500)
    private String addressLine1;

    @Column(length = 500)
    private String addressLine2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "state_id",
            nullable = false
    )
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "city_id",
            nullable = false
    )
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "pincode_id",
            nullable = false
    )
    private CityPincode pincode;

    @Builder.Default
    @OneToMany(
            mappedBy = "priest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PriestLanguageMapping> languages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "community_id",
            nullable = false
    )
    private Community community;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriestExperience experience;

    @Column(length = 100)
    private String referredBy;

    private String bankingName;

    private String bankName;

    private String bankBranchName;

    private String bankIfscCode;

    private String bankAccountNumber;

    private String upiId;

    private String priestPhotoUrl;

    private String aadhaarPdfUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    @Column(nullable = false,
            updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
