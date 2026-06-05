package com.example.sreepooja.Entity;

import com.example.sreepooja.Entity.Bookings.Booking;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(name = "idx_payment_order_id", columnList = "razorpayOrderId"),
                @Index(name = "idx_payment_payment_id", columnList = "razorpayPaymentId")
        }
)
@Getter
@Setter
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "booking_id",
            nullable = false
    )
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private Users user;

    @Column(length = 200)
    private String razorpayOrderId;

    @Column(length = 200)
    private String razorpayPaymentId;

    @Column(length = 500)
    private String razorpaySignature;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;
}