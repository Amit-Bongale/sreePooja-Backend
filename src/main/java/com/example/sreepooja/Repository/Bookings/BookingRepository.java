package com.example.sreepooja.Repository.Bookings;

import com.example.sreepooja.Entity.Bookings.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserId(
            Long userId,
            Pageable pageable
    );

}
