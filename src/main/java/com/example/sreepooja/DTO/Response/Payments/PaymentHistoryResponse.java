package com.example.sreepooja.DTO.Response.Payments;

import com.example.sreepooja.Enum.Bookings.PaymentOption;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentHistoryResponse {

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private PaymentOption paymentOption;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private LocalDateTime paidAt;
}
