package com.example.sreepooja.Service.Bookings;

import com.example.sreepooja.DTO.Request.Bookings.*;
import com.example.sreepooja.DTO.Response.Bookings.*;
import com.example.sreepooja.DTO.Users.UserResponse;
import org.springframework.data.domain.Page;

public interface BookingService {

    CheckoutResponse getCheckout(Long packageId);

    CreateBookingResponse createBooking(
            CreateBookingRequest request
    );

    Page<MyBookingResponse> getMyBookings(
            int page,
            int size
    );

    BookingDetailsResponse getBookingDetails(
            Long bookingId
    );

    Page<AdminBookingResponse> getAllBookings(
            AdminBookingFilterRequest request,
            int page,
            int size
    );

    AdminBookingDetailsResponse
    getAdminBookingDetails(
            Long bookingId
    );

    ConfirmBookingResponse confirmBooking(
            Long bookingId,
            ConfirmBookingRequest request
    );

    ConfirmBookingResponse reassignBooking(
            Long bookingId,
            ConfirmBookingRequest request
    );

    ConfirmBookingResponse cancelBooking(
            Long bookingId
    );

    ConfirmBookingResponse completeBooking(
            Long bookingId
    );

    String createCustomBooking(
            CreateCustomBookingRequest request
    );

    String respondCustomBooking(
            Long bookingId,
            RespondCustomBookingRequest request
    );

    UserResponse getUserByMobileNo(String mobileNo);

}
