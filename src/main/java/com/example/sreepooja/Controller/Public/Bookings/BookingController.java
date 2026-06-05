package com.example.sreepooja.Controller.Public.Bookings;

import com.example.sreepooja.DTO.Request.Bookings.CreateBookingRequest;
import com.example.sreepooja.DTO.Response.Bookings.CheckoutResponse;
import com.example.sreepooja.DTO.Response.Bookings.CreateBookingResponse;
import com.example.sreepooja.Service.Bookings.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/checkout/{packageId}")
    public ResponseEntity<CheckoutResponse> getCheckout(
            @PathVariable Long packageId
    ) {

        return ResponseEntity.ok(
                bookingService.getCheckout(packageId)
        );
    }

    @PostMapping
    public ResponseEntity<CreateBookingResponse>
    createBooking(
            @Valid
            @RequestBody
            CreateBookingRequest request
    ) {

        return ResponseEntity.ok(
                bookingService.createBooking(request)
        );
    }
}
