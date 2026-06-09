package com.example.sreepooja.DTO.Request.Payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyPaymentRequest {

    @NotBlank
    @JsonProperty("razorpay_order_id")
    private String razorpayOrderId;

    @NotBlank
    @JsonProperty("razorpay_payment_id")
    private String razorpayPaymentId;

    @NotBlank
    @JsonProperty("razorpay_signature")
    private String razorpaySignature;
}