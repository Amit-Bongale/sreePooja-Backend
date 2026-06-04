package com.example.sreepooja.DTO.Response.Bookings;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBookingResponse {

    private Long bookingId;

    private String bookingNumber;

    private String razorpayOrderId;

    private BigDecimal amountToPay;

    private String currency;
}
