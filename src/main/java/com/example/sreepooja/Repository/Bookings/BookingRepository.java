package com.example.sreepooja.Repository.Bookings;

import com.example.sreepooja.Entity.Bookings.Booking;
import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    Page<Booking> findByUserIdAndBookingStatus(
            Long userId,
            BookingStatus bookingStatus,
            Pageable pageable
    );

    List<Booking> findByPaymentStatus(
            PaymentStatus paymentStatus
    );
}
