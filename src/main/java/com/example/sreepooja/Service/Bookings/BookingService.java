package com.example.sreepooja.Service.Bookings;

import com.example.sreepooja.DTO.Request.Bookings.CreateBookingRequest;
import com.example.sreepooja.DTO.Response.Bookings.BookingDetailsResponse;
import com.example.sreepooja.DTO.Response.Bookings.CheckoutResponse;
import com.example.sreepooja.DTO.Response.Bookings.CreateBookingResponse;
import com.example.sreepooja.DTO.Response.Bookings.MyBookingResponse;
import org.springframework.data.domain.Page;

public interface BookingService {

    CheckoutResponse getCheckout(Long packageId);

    CreateBookingResponse createBooking(
            CreateBookingRequest request
    );

    Page<MyBookingResponse> getMyBookings(
            int page,
            int size
    );

    BookingDetailsResponse getBookingDetails(
            Long bookingId
    );

}
