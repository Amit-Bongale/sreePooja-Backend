package com.example.sreepooja.Repository;

import com.example.sreepooja.Entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payments, Long> {

    Optional<Payments> findByRazorpayOrderId(
            String razorpayOrderId
    );

    boolean existsByRazorpayPaymentId(
            String razorpayPaymentId
    );

    List<Payments> findByBookingId(
            Long bookingId
    );
}