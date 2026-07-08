package com.example.sreepooja.Controller.Admin.Bookings;

import com.example.sreepooja.DTO.Request.Bookings.AdminBookingFilterRequest;
import com.example.sreepooja.DTO.Request.Bookings.ConfirmBookingRequest;
import com.example.sreepooja.DTO.Request.Bookings.RespondCustomBookingRequest;
import com.example.sreepooja.DTO.Response.Bookings.AdminBookingDetailsResponse;
import com.example.sreepooja.DTO.Response.Bookings.AdminBookingResponse;
import com.example.sreepooja.DTO.Response.Bookings.ConfirmBookingResponse;
import com.example.sreepooja.DTO.Users.UserResponse;
import com.example.sreepooja.Service.Bookings.BookingService;
import jakarta.validation.Valid;
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

    @GetMapping("/{bookingId}")
    public ResponseEntity<AdminBookingDetailsResponse>
    getAdminBookingDetails(
            @PathVariable Long bookingId
    ) {

        return ResponseEntity.ok(
                bookingService.getAdminBookingDetails(
                        bookingId
                )
        );
    }

    @PutMapping(
            "/{bookingId}/confirm"
    )
    public ResponseEntity<ConfirmBookingResponse>
    confirmBooking(

            @PathVariable
            Long bookingId,

            @Valid
            @RequestBody
            ConfirmBookingRequest request
    ) {

        return ResponseEntity.ok(
                bookingService.confirmBooking(
                        bookingId,
                        request
                )
        );
    }

    @PutMapping(
            "/{bookingId}/reassign"
    )
    public ResponseEntity<ConfirmBookingResponse>
    reassignBooking(

            @PathVariable
            Long bookingId,

            @Valid
            @RequestBody
            ConfirmBookingRequest request
    ) {

        return ResponseEntity.ok(

                bookingService.reassignBooking(
                        bookingId,
                        request
                )
        );
    }

    @PutMapping(
            "/{bookingId}/cancel"
    )
    public ResponseEntity<ConfirmBookingResponse>
    cancelBooking(

            @PathVariable
            Long bookingId
    ) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(
                        bookingId
                )
        );
    }

    @PutMapping(
            "/{bookingId}/complete"
    )
    public ResponseEntity<ConfirmBookingResponse>
    completeBooking(

            @PathVariable
            Long bookingId
    ) {

        return ResponseEntity.ok(
                bookingService.completeBooking(
                        bookingId
                )
        );
    }

    @PutMapping("/custom/{bookingId}/respond")
    public ResponseEntity<String> respondCustomBooking(
            @PathVariable Long bookingId,
            @Valid @RequestBody RespondCustomBookingRequest request
    ) {

        return ResponseEntity.ok(
                bookingService.respondCustomBooking(
                        bookingId,
                        request
                )
        );
    }

    @GetMapping("/users/search")
    public ResponseEntity<UserResponse> getUserByMobileNo(
            @RequestParam String mobileNo
    ) {

        return ResponseEntity.ok(
                bookingService.getUserByMobileNo(mobileNo)
        );
    }
}
