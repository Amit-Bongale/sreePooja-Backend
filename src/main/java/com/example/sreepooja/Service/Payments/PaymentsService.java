package com.example.sreepooja.Service.Payments;

import com.example.sreepooja.DTO.Request.Payments.VerifyPaymentRequest;
import com.example.sreepooja.DTO.Response.Payments.CreateOrderResponse;
import com.example.sreepooja.DTO.Response.Payments.VerifyPaymentResponse;

public interface PaymentsService {

    CreateOrderResponse createOrder(Long bookingId);

    VerifyPaymentResponse verifyPayment(
            VerifyPaymentRequest request
    );

    CreateOrderResponse createBalanceOrder(
            Long bookingId
    );
}
