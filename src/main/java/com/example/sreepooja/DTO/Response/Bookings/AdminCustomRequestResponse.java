package com.example.sreepooja.DTO.Response.Bookings;

import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import com.example.sreepooja.Enum.Bookings.TimeSlot;
import com.example.sreepooja.Enum.Poojas.PackageType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AdminCustomRequestResponse {

    private Long bookingId;

    private String bookingNumber;

    private String customerFirstName;

    private String customerLastName;

    private String mobileNumber;

    private String serviceName;

    private LocalDate preferredDate;

    private BookingStatus bookingStatus;

    private String address;

    private String city;

    private PackageType packageType;

    private PaymentStatus paymentStatus;

    private TimeSlot preferredTimeSlot;

    private String state;

    private LocalDate poojaDate;

    private LocalTime poojaTime;
}