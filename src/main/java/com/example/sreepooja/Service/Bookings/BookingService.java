package com.example.sreepooja.Service.Bookings;

import com.example.sreepooja.DTO.Response.Bookings.CheckoutResponse;

public interface BookingService {
    CheckoutResponse getCheckout(Long packageId);
}
