package com.example.sreepooja.Service.Bookings;

import com.example.sreepooja.DTO.Request.Bookings.CreateBookingRequest;
import com.example.sreepooja.DTO.Response.Bookings.CheckoutResponse;
import com.example.sreepooja.DTO.Response.Bookings.CreateBookingResponse;

public interface BookingService {

    CheckoutResponse getCheckout(Long packageId);

    CreateBookingResponse createBooking(
            CreateBookingRequest request
    );

}
