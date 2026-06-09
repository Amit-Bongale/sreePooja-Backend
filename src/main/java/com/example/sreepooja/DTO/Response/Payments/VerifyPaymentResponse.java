package com.example.sreepooja.DTO.Response.Payments;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyPaymentResponse {

    private String bookingNumber;

    private String paymentStatus;

    private String bookingStatus;

    private String message;
}