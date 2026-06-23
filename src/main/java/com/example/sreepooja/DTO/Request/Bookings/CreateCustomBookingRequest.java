package com.example.sreepooja.DTO.Request.Bookings;

import com.example.sreepooja.Enum.Bookings.TimeSlot;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class CreateCustomBookingRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long serviceId;

    @NotBlank
    private String customDescription;

    @NotNull
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
    @Positive
    private BigDecimal totalAmount;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal advancePercentage;
}