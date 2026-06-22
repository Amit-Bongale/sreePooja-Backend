package com.example.sreepooja.DTO.Response.Bookings;

import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentOption;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import com.example.sreepooja.Enum.Poojas.PackageType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class MyBookingResponse {

    private Long bookingId;

    private String bookingNumber;

    private String serviceName;

    private String thumbnailImage;

    private PackageType packageType;

    private LocalDate poojaDate;

    private LocalTime poojaTime;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private PaymentOption paymentOption;

    private BigDecimal totalAmount;

    private BigDecimal balanceAmount;

    private Boolean showPayBalanceButton;

    private String paymentMessage;

    private String priestName;

    private String address;
}