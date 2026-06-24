package com.example.sreepooja.DTO.Request.Bookings;

import com.example.sreepooja.Enum.Bookings.PaymentOption;
import com.example.sreepooja.Enum.Bookings.TimeSlot;
import com.example.sreepooja.Enum.Poojas.PackageType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBookingRequest {

    @NotNull
    private Long packageId;

    @NotNull
    @Future
    private LocalDate preferredDate;

    @NotNull
    private TimeSlot preferredTimeSlot;

    private String preferredLanguage;

    private String preferredCommunity;

    @NotBlank
    private String address;

    @NotNull
    private Long stateId;

    @NotNull
    private Long cityId;

    @NotNull
    private Long pincodeId;

    private String specialInstructions;

    @NotNull
    private PaymentOption paymentOption;

    @NotNull
    private PackageType packageType;
}