package com.example.sreepooja.Controller.Public.Bookings;

import com.example.sreepooja.DTO.Request.Bookings.CreateBookingRequest;
import com.example.sreepooja.DTO.Request.Bookings.CreateCustomBookingRequest;
import com.example.sreepooja.DTO.Response.Bookings.BookingDetailsResponse;
import com.example.sreepooja.DTO.Response.Bookings.CheckoutResponse;
import com.example.sreepooja.DTO.Response.Bookings.CreateBookingResponse;
import com.example.sreepooja.DTO.Response.Bookings.MyBookingResponse;
import com.example.sreepooja.Service.Bookings.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("/my-bookings")
    public ResponseEntity<Page<MyBookingResponse>>
    getMyBookings(

            @RequestParam(
                    defaultValue = "0"
            ) int page,

            @RequestParam(
                    defaultValue = "5"
            ) int size
    ) {

        return ResponseEntity.ok(
                bookingService.getMyBookings(
                        page,
                        size
                )
        );
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDetailsResponse>
    getBookingDetails(
            @PathVariable Long bookingId
    ) {

        return ResponseEntity.ok(
                bookingService.getBookingDetails(
                        bookingId
                )
        );
    }

    @PostMapping("/custom")
    public ResponseEntity<String> createCustomBooking(
            @Valid @RequestBody CreateCustomBookingRequest request
    ) {

        return ResponseEntity.ok(
                bookingService.createCustomBooking(request)
        );
    }
}
