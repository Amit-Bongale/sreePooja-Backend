package com.example.sreepooja.Service.Payments;

import com.example.sreepooja.DTO.Response.Payments.CreateOrderResponse;

public interface PaymentsService {

    CreateOrderResponse createOrder(Long bookingId);
}
