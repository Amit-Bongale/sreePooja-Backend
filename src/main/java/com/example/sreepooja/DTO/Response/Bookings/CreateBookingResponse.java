package com.example.sreepooja.DTO.Response.Bookings;

import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentOption;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CreateBookingResponse {

    private Long bookingId;

    private String bookingNumber;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private String razorpayOrderId;

    private BigDecimal amountToPay;

    private PaymentOption paymentOption;
}
