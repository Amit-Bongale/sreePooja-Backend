package com.example.sreepooja.DTO.Response.Bookings;

import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import com.example.sreepooja.Enum.Poojas.PackageType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class AdminBookingResponse {

    private Long bookingId;

    private String bookingNumber;

    private String customerFirstName;

    private String customerLastName;

    private String mobileNumber;

    private String serviceName;

    private PackageType packageType;

    private LocalDate poojaDate;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;

    private BigDecimal balanceAmount;
}
