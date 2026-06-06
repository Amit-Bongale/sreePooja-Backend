package com.example.sreepooja.Service.Payments;

import com.example.sreepooja.DTO.Response.Payments.CreateOrderResponse;
import com.example.sreepooja.Entity.Bookings.Booking;
import com.example.sreepooja.Entity.Payments;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentOption;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Bookings.BookingRepository;
import com.example.sreepooja.Repository.PaymentRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
import com.example.sreepooja.Service.CustomUserDetails.CustomUserDetails;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService{

    private final BookingRepository bookingRepository;

    private final PaymentRepository paymentRepository;

    private final RazorpayClient razorpayClient;

    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(
            Long bookingId
    ) {

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

        Users user =
                usersRepository
                        .findById(userDetails.getUserId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Booking not found"
                                )
                        );

        if (!booking.getUser().getId()
                .equals(userDetails.getUserId())) {

            throw new BadRequestException(
                    "Booking does not belong to user"
            );
        }

        if (booking.getBookingStatus()
                != BookingStatus.PENDING_PAYMENT) {

            throw new BadRequestException(
                    "Payment already initiated"
            );
        }

        BigDecimal payableAmount;

        if (booking.getPaymentOption()
                == PaymentOption.ADVANCE) {

            payableAmount =
                    booking.getAdvanceAmount();

        } else {

            payableAmount =
                    booking.getTotalAmount();
        }

        try {

            JSONObject orderRequest =
                    new JSONObject();

            orderRequest.put(
                    "amount",
                    payableAmount
                            .multiply(BigDecimal.valueOf(100))
                            .intValue()
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            orderRequest.put(
                    "receipt",
                    booking.getBookingNumber()
            );

            Order razorpayOrder =
                    razorpayClient.orders
                            .create(orderRequest);

            Payments payment =
                    new Payments();

            payment.setBooking(booking);

            payment.setUser(user);

            payment.setAmount(payableAmount);

            payment.setRazorpayOrderId(
                    razorpayOrder.get("id")
            );

            payment.setStatus(
                    PaymentStatus.PENDING
            );

            paymentRepository.save(payment);

            return CreateOrderResponse.builder()
                    .bookingId(booking.getId())
                    .bookingNumber(
                            booking.getBookingNumber()
                    )
                    .razorpayOrderId(
                            razorpayOrder.get("id")
                    )
                    .amount(payableAmount)
                    .currency("INR")
                    .build();

        } catch (Exception e) {

            throw new BadRequestException(
                    "Unable to create payment order"
            );
        }
    }
}
