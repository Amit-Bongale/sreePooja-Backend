package com.example.sreepooja.DTO.Request.Payments;

import com.example.sreepooja.Enum.Bookings.PaymentOption;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotNull
    private Long bookingId;

    private PaymentOption paymentOption;
}
