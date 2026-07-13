package com.example.sreepooja.DTO.Response.Bookings;

import com.example.sreepooja.DTO.Response.Payments.PaymentHistoryResponse;
import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentOption;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import com.example.sreepooja.Enum.Bookings.TimeSlot;
import com.example.sreepooja.Enum.Poojas.PackageType;
import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class AdminBookingDetailsResponse {

    private Long bookingId;

    private String bookingNumber;

    private String customerFirstName;

    private String customerLastName;

    private String mobileNumber;

    private String serviceName;

    private PackageType packageType;

    private String preferredLanguage;

    private String preferredCommunity;

    private String address;

    private String state;

    private String city;

    private String pincode;

    private LocalDate preferredDate;

    private TimeSlot preferredTimeSlot;

    private LocalDate confirmedDate;

    private LocalTime confirmedTime;

    private String priestName;

    private String specialInstructions;

    private String customDescription;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private PaymentOption paymentOption;

    private BigDecimal packagePrice;

    @DecimalMin("0.00")
    private BigDecimal advancePercentage;

//    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private BigDecimal advanceAmount;

    private BigDecimal balanceAmount;

    private List<PaymentHistoryResponse> payments;

    private LocalDateTime bookedAt;
}