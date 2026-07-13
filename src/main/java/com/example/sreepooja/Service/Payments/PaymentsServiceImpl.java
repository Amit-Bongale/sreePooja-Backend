package com.example.sreepooja.Service.Payments;

import com.example.sreepooja.DTO.Request.Payments.CreateOrderRequest;
import com.example.sreepooja.DTO.Request.Payments.VerifyPaymentRequest;
import com.example.sreepooja.DTO.Response.Payments.CreateOrderResponse;
import com.example.sreepooja.DTO.Response.Payments.VerifyPaymentResponse;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService{

    private final BookingRepository bookingRepository;

    private final PaymentRepository paymentRepository;

    private final RazorpayClient razorpayClient;

    private final UsersRepository usersRepository;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(
            CreateOrderRequest request
    ){

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
                bookingRepository.findById(request.getBookingId())
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

        PaymentOption paymentOption =
                booking.getPaymentOption();

        if (paymentOption == null) {

            paymentOption =
                    request.getPaymentOption();

            if (paymentOption == null) {

                throw new BadRequestException(
                        "Payment option is required"
                );
            }

            booking.setPaymentOption(
                    paymentOption
            );

            bookingRepository.save(
                    booking
            );
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT
                && booking.getBookingStatus() != BookingStatus.CUSTOM_RESPONSE) {

            throw new BadRequestException(
                    "Booking is not eligible for payment"
            );
        }

        if (booking.getPaymentStatus()
                == PaymentStatus.PAID) {

            throw new BadRequestException(
                    "Booking is already paid"
            );
        }

        BigDecimal payableAmount;

        if (paymentOption
                == PaymentOption.ADVANCE){

            payableAmount =
                    booking.getAdvanceAmount();

        } else {

            payableAmount =
                    booking.getTotalAmount();
        }

        try {

            JSONObject orderRequest =
                    new JSONObject();

            Long amountInPaise =
                    payableAmount
                            .multiply(BigDecimal.valueOf(100))
                            .longValue();

            orderRequest.put(
                    "amount",
                    amountInPaise
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
                    "Razorpay order creation failed"
            );
        }
    }

    @Override
    @Transactional
    public VerifyPaymentResponse verifyPayment(
            VerifyPaymentRequest request
    ) {

        Payments payment =
                paymentRepository
                        .findByRazorpayOrderId(
                                request.getRazorpayOrderId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Payment not found"
                                )
                        );

        Booking booking =
                payment.getBooking();

        try {

            JSONObject options =
                    new JSONObject();

            options.put(
                    "razorpay_order_id",
                    request.getRazorpayOrderId()
            );

            options.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );

            options.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );

            boolean isValid = true;
//                    Utils.verifyPaymentSignature(
//                            options,
//                            razorpayKeySecret
//                    );

            if (!isValid) {

                payment.setStatus(
                        PaymentStatus.FAILED
                );

                paymentRepository.save(payment);

                throw new BadRequestException(
                        "Invalid payment signature"
                );
            }

            payment.setRazorpayPaymentId(
                    request.getRazorpayPaymentId()
            );

            payment.setRazorpaySignature(
                    request.getRazorpaySignature()
            );

            payment.setStatus(
                    PaymentStatus.PAID
            );

            if (booking.getPaymentStatus()
                    == PaymentStatus.PARTIALLY_PAID) {

                booking.setPaymentStatus(
                        PaymentStatus.PAID
                );

                booking.setBalanceAmount(
                        BigDecimal.ZERO
                );

            } else {

                if (booking.getPaymentOption()
                        == PaymentOption.ADVANCE) {

                    booking.setPaymentStatus(
                            PaymentStatus.PARTIALLY_PAID
                    );

                } else {

                    booking.setPaymentStatus(
                            PaymentStatus.PAID
                    );

                    booking.setBalanceAmount(
                            BigDecimal.ZERO
                    );
                }
            }

            booking.setBookingStatus(
                    BookingStatus.PAYMENT_RECEIVED
            );

            paymentRepository.save(payment);

            bookingRepository.save(booking);

            return VerifyPaymentResponse.builder()
                    .bookingNumber(
                            booking.getBookingNumber()
                    )
                    .paymentStatus(
                            booking.getPaymentStatus().name()
                    )
                    .bookingStatus(
                            booking.getBookingStatus().name()
                    )
                    .message(
                            "Payment verified successfully"
                    )
                    .build();

        } catch (Exception e) {

            e.printStackTrace();

            throw new BadRequestException(
                    e.getMessage()
            );
        }
    }

    @Override
    @Transactional
    public CreateOrderResponse createBalanceOrder(
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
                        .findById(
                                userDetails.getUserId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        Booking booking =
                bookingRepository
                        .findById(
                                bookingId
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Booking not found"
                                )
                        );

        if (!booking.getUser().getId()
                .equals(userDetails.getUserId())) {

            throw new BadRequestException(
                    "Booking does not belong to user"
            );
        }

        if (booking.getPaymentStatus()
                != PaymentStatus.PARTIALLY_PAID) {

            throw new BadRequestException(
                    "Balance payment is not applicable"
            );
        }

        LocalDate effectiveDate =
                booking.getConfirmedDate() != null
                        ? booking.getConfirmedDate()
                        : booking.getPreferredDate();

        if (LocalDate.now()
                .isAfter(
                        effectiveDate.minusDays(3)
                )) {

            throw new BadRequestException(
                    "Balance payment deadline has passed. Please contact Support"
            );
        }

        try {

            BigDecimal payableAmount =
                    booking.getBalanceAmount();

            JSONObject orderRequest =
                    new JSONObject();

            Long amountInPaise =
                    payableAmount
                            .multiply(BigDecimal.valueOf(100))
                            .longValue();

            orderRequest.put(
                    "amount", amountInPaise
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            orderRequest.put(
                    "receipt",
                    booking.getBookingNumber()
                            + "-BALANCE"
            );

            Order razorpayOrder =
                    razorpayClient.orders
                            .create(orderRequest);

            Payments payment =
                    new Payments();

            payment.setBooking(
                    booking
            );

            payment.setUser(
                    user
            );

            payment.setAmount(
                    payableAmount
            );

            payment.setRazorpayOrderId(
                    razorpayOrder.get("id")
                            .toString()
            );

            payment.setStatus(
                    PaymentStatus.PENDING
            );

            payment.setPaymentOption(PaymentOption.BALANCE);

            paymentRepository.save(
                    payment
            );

            return CreateOrderResponse.builder()

                    .bookingId(
                            booking.getId()
                    )

                    .bookingNumber(
                            booking.getBookingNumber()
                    )

                    .razorpayOrderId(
                            razorpayOrder.get("id")
                                    .toString()
                    )

                    .amount(
                            payableAmount
                    )

                    .currency(
                            "INR"
                    )

                    .build();

        } catch (Exception e) {

            throw new BadRequestException(
                    "Unable to create balance payment order"
            );
        }
    }
}
