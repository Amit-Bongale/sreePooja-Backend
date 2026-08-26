package com.example.sreepooja.Repository;

import com.example.sreepooja.Entity.Payments;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    @Query("""
       SELECT COALESCE(SUM(p.amount), 0)
       FROM Payments p
       WHERE p.status = :status
       """)
    BigDecimal calculateTotalRevenue(
            @Param("status")
            PaymentStatus status
    );

    List<Payments> findByBookingIdOrderByCreatedAtAsc(Long bookingId);

    List<Payments> findByBookingIdInOrderByBookingIdAscCreatedAtAsc(
            List<Long> bookingIds
    );
}