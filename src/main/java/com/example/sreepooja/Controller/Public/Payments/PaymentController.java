package com.example.sreepooja.Controller.Public.Payments;

import com.example.sreepooja.DTO.Request.Payments.VerifyPaymentRequest;
import com.example.sreepooja.DTO.Response.Payments.CreateOrderResponse;
import com.example.sreepooja.DTO.Response.Payments.VerifyPaymentResponse;
import com.example.sreepooja.Service.Payments.PaymentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentsService paymentService;

    @PostMapping("/create-order/{bookingId}")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @PathVariable Long bookingId
    ) {

        return ResponseEntity.ok(
                paymentService.createOrder(bookingId)
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyPaymentResponse> verifyPayment(
            @Valid
            @RequestBody
            VerifyPaymentRequest request
    ) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(request)
        );
    }

    @PostMapping("/create-balance-order/{bookingId}")
    public ResponseEntity<CreateOrderResponse>
    createBalanceOrder(
            @PathVariable Long bookingId
    ) {

        return ResponseEntity.ok(
                paymentService.createBalanceOrder(
                        bookingId
                )
        );
    }
}
