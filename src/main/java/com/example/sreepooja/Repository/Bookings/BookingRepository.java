package com.example.sreepooja.Repository.Bookings;

import com.example.sreepooja.Entity.Bookings.Booking;
import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import com.example.sreepooja.Enum.Poojas.PackageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    @Query("""
    SELECT b
    FROM Booking b
    WHERE b.user.id = :userId
      AND (
            b.bookingStatus <> :paymentPending
            OR b.packageType = :custom
      )
""")
    Page<Booking> findMyBookings(
            @Param("userId") Long userId,
            @Param("paymentPending") BookingStatus paymentPending,
            @Param("custom") PackageType custom,
            Pageable pageable
    );

    List<Booking> findByPaymentStatus(
            PaymentStatus paymentStatus
    );

    long countByBookingStatusIn(Collection<BookingStatus> bookingStatuses);

    long countByBookingStatusNotIn(Collection<BookingStatus> statuses);

    Page<Booking> findByBookingStatusOrderByCreatedAtDesc(
            BookingStatus bookingStatus,
            Pageable pageable
    );
}
