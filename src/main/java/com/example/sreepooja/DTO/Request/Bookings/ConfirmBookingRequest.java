package com.example.sreepooja.DTO.Request.Bookings;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ConfirmBookingRequest {

    @NotNull
    private Long priestId;

    @NotNull
    @Future
    private LocalDate confirmedDate;

    @NotNull
    private LocalTime confirmedTime;
}
