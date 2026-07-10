package com.example.sreepooja.Entity.Priests;

import com.example.sreepooja.Entity.Masters.City;
import com.example.sreepooja.Entity.Masters.CityPincode;
import com.example.sreepooja.Entity.Masters.Community;
import com.example.sreepooja.Entity.Masters.State;
import com.example.sreepooja.Enum.Priests.PriestExperience;
import com.example.sreepooja.Enum.Priests.PriestRegistrationStatus;
import com.example.sreepooja.Enum.Priests.PriestRegistrationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "priest_registrations",
        indexes = {
                @Index(
                        name = "idx_priest_registration_mobile",
                        columnList = "mobileNumber"
                ),
                @Index(
                        name = "idx_priest_registration_aadhaar",
                        columnList = "aadhaarNumber"
                ),
                @Index(
                        name = "idx_priest_registration_status",
                        columnList = "status"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriestRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String mobileNumber;

    private String whatsappNumber;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private String gothra;

    private String pravara;

    @Column(nullable = false)
    private String nativePlace;

    @Column(nullable = false, unique = true)
    private String aadhaarNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriestExperience experience;

    private String referredBy;

    @Column(nullable = false)
    private String addressLine1;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "community_id",
            nullable = false
    )
    private Community community;

    @OneToMany(
            mappedBy = "registration",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PriestRegistrationLanguageMapping> languages =
            new ArrayList<>();

    @Column(nullable = false)
    private String bankingName;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String bankBranchName;

    @Column(nullable = false)
    private String bankIfscCode;

    @Column(nullable = false)
    private String bankAccountNumber;

    private String upiId;

    @Column(nullable = false)
    private String priestPhotoUrl;

    @Column(nullable = false)
    private String aadhaarPdfUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PriestRegistrationStatus status =
            PriestRegistrationStatus.PENDING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}