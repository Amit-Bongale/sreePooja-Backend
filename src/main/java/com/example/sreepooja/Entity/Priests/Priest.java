package com.example.sreepooja.Entity.Priests;

import com.example.sreepooja.Entity.Masters.Community;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.Priests.PriestExperience;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    @Column(nullable = false, length = 150)
    private String city;

    @Column(nullable = false, length = 150)
    private String state;

    @Column(nullable = false, length = 10)
    private String pincode;

    @Column(length = 500)
    private String languagesSpoken;

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
