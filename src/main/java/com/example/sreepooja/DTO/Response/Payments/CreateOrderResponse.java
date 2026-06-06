package com.example.sreepooja.DTO.Response.Payments;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CreateOrderResponse {

    private Long bookingId;

    private String bookingNumber;

    private String razorpayOrderId;

    private BigDecimal amount;

    private String currency;
}
