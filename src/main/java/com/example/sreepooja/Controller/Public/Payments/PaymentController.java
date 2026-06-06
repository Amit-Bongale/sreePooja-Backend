package com.example.sreepooja.Controller.Public.Payments;

import com.example.sreepooja.DTO.Response.Payments.CreateOrderResponse;
import com.example.sreepooja.Service.Payments.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
