package com.example.sreepooja.Entity.Bookings;

import com.example.sreepooja.Entity.Masters.City;
import com.example.sreepooja.Entity.Masters.CityPincode;
import com.example.sreepooja.Entity.Masters.State;
import com.example.sreepooja.Entity.Poojas.PoojaServices;
import com.example.sreepooja.Entity.Poojas.ServicePackage;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.TimeSlot;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "bookings",
        indexes = {
                @Index(name = "idx_booking_number", columnList = "bookingNumber"),
                @Index(name = "idx_booking_status", columnList = "bookingStatus"),
                @Index(name = "idx_booking_date", columnList = "preferredDate")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String bookingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private PoojaServices service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private ServicePackage selectedPackage;

    @Column(nullable = false)
    private LocalDate preferredDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeSlot preferredTimeSlot;

    private LocalDate confirmedDate;

    @Enumerated(EnumType.STRING)
    private TimeSlot confirmedTimeSlot;

    @Column(length = 150)
    private String priestName;

    @Column(length = 100)
    private String preferredLanguage;

    @Column(length = 100)
    private String preferredCommunity;

    @Column(nullable = false, length = 2000)
    private String addressLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pincode_id", nullable = false)
    private CityPincode pincode;

    @Column(length = 1000)
    private String specialInstructions;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal packagePrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal advancePercentage;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal advanceAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balanceAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}