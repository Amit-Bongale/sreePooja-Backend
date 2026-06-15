package com.example.sreepooja.DTO.Request.Bookings;

import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminBookingFilterRequest {

    private Long bookingId;

    private String mobileNumber;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private LocalDate fromDate;

    private LocalDate toDate;

}