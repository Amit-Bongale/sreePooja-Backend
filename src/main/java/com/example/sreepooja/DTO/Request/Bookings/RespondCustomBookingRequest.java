package com.example.sreepooja.DTO.Request.Bookings;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RespondCustomBookingRequest {

    @NotNull
    @Future
    private LocalDate confirmedDate;

    @NotNull
    private LocalTime confirmedTime;

    @NotBlank
    private String customDescription;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal packagePrice;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal advancePercentage;
}