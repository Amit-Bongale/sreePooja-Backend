package com.example.sreepooja.DTO.Response.Bookings;

import com.example.sreepooja.Enum.Bookings.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ConfirmBookingResponse {

    private Long bookingId;

    private String bookingNumber;

    private String priestName;

    private LocalDate confirmedDate;

    private LocalTime confirmedTime;

    private BookingStatus bookingStatus;
}
