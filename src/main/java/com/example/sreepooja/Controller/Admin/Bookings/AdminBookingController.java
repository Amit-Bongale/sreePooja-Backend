package com.example.sreepooja.Controller.Admin.Bookings;

import com.example.sreepooja.DTO.Request.Bookings.AdminBookingFilterRequest;
import com.example.sreepooja.DTO.Response.Bookings.AdminBookingResponse;
import com.example.sreepooja.Service.Bookings.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<Page<AdminBookingResponse>>
    getAllBookings(

            @ModelAttribute
            AdminBookingFilterRequest request,

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ) {

        return ResponseEntity.ok(

                bookingService.getAllBookings(
                        request,
                        page,
                        size
                )
        );
    }
}
